package io.github.pbernet

import java.io.IOException

import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.{Flow, Sink, Source}

import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._

import scala.concurrent.Future
import scala.math._

case class IpInfo(ip: String, country: Option[String], city: Option[String], latitude: Option[Double], longitude: Option[Double])

case class IpPairSummaryRequest(ip1: String, ip2: String)

case class IpPairSummary(distance: Option[Double], ip1Info: IpInfo, ip2Info: IpInfo)

object IpPairSummary {
  def apply(ip1Info: IpInfo, ip2Info: IpInfo): IpPairSummary = IpPairSummary(calculateDistance(ip1Info, ip2Info), ip1Info, ip2Info)

  private def calculateDistance(ip1Info: IpInfo, ip2Info: IpInfo): Option[Double] = {
    (ip1Info.latitude, ip1Info.longitude, ip2Info.latitude, ip2Info.longitude) match {
      case (Some(lat1), Some(lon1), Some(lat2), Some(lon2)) =>
        // see http://www.movable-type.co.uk/scripts/latlong.html
        val φ1 = toRadians(lat1)
        val φ2 = toRadians(lat2)
        val Δφ = toRadians(lat2 - lat1)
        val Δλ = toRadians(lon2 - lon1)
        val a = pow(sin(Δφ / 2), 2) + cos(φ1) * cos(φ2) * pow(sin(Δλ / 2), 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        Option(EarthRadius * c)
      case _ => None
    }
  }

  private val EarthRadius = 6371.0
}

trait WinticketService extends BaseService {

  lazy val telizeConnectionFlow: Flow[HttpRequest, HttpResponse, Any] =
  //Use Connection-Level Client-Side API
    Http().outgoingConnection(telizeHost, telizePort)

  def telizeRequest(request: HttpRequest): Future[HttpResponse] = Source.single(request).via(telizeConnectionFlow).runWith(Sink.head)

  def fetchIpInfo(ip: String): Future[Either[String, IpInfo]] = {
    telizeRequest(RequestBuilding.Get(s"/geoip/$ip")).flatMap { response =>
      response.status match {
        case OK => Unmarshal(response.entity).to[IpInfo].map(Right(_))
        case BadRequest => Future.successful(Left(s"$ip: incorrect IP format"))
        case _ => Unmarshal(response.entity).to[String].flatMap { entity =>
          val error = s"Telize request failed with status code ${response.status} and entity $entity"
          log.error(error)
          Future.failed(new IOException(error))
        }
      }
    }
  }

  val routes = logRequestResult("akka-http-microservice") {

    pathPrefix("ip") {
      (get & path(Segment)) { ip =>
        complete {
          fetchIpInfo(ip).map[ToResponseMarshallable] {
            case Right(ipInfo) => ipInfo
            case Left(errorMessage) => BadRequest -> errorMessage
          }
        }
      } ~
        (post & entity(as[IpPairSummaryRequest])) { ipPairSummaryRequest =>
          complete {
            val ip1InfoFuture = fetchIpInfo(ipPairSummaryRequest.ip1)
            val ip2InfoFuture = fetchIpInfo(ipPairSummaryRequest.ip2)
            ip1InfoFuture.zip(ip2InfoFuture).map[ToResponseMarshallable] {
              case (Right(info1), Right(info2)) => IpPairSummary(info1, info2)
              case (Left(errorMessage), _) => BadRequest -> errorMessage
              case (_, Left(errorMessage)) => BadRequest -> errorMessage
            }
          }
        }
    } ~
      pathPrefix("xx") {
        (get & path(Segment)) { value =>
          //Additional Test
          complete {
            //http://doc.akka.io/docs/akka-stream-and-http-experimental/1.0/scala/http/common/xml-support.html
          <html>
            <body>Hello world! Value: {value}</body>
          </html>
          }
        }
      }
  }
}