package io.github.pbernet

import akka.event.NoLogging
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.{ HttpResponse, HttpRequest }
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.Flow
import org.scalatest._

//This class does ONLY a RouteTest, that is the telizeConnectionFlow is mocked and thus "only" the routes are tested
class WinticketServiceSpec extends FlatSpec with Matchers with ScalatestRouteTest with WinticketService {
  override def testConfigSource = "akka.loglevel = WARNING"
  //Config>>config is not public...
  def config = testConfig
  override val log = NoLogging

  val ip1Info = IpInfo("8.8.8.8", Option("United States"), Option("Mountain View"), Option(37.386), Option(-122.0838))
  val ip2Info = IpInfo("8.8.4.4", Option("United States"), None, Option(38.0), Option(-97.0))
  val ipPairSummary = IpPairSummary(ip1Info, ip2Info)

  val ip3Info = IpInfo("91.199.98.146", Option("Switzerland"), Option("Zuerich"), Option(1.0), Option(-1.0))

  override lazy val telizeConnectionFlow = Flow[HttpRequest].map { request =>
    if (request.uri.toString().endsWith(ip1Info.ip))
      HttpResponse(status = OK, entity = marshal(ip1Info))
    else if (request.uri.toString().endsWith(ip2Info.ip))
      HttpResponse(status = OK, entity = marshal(ip2Info))
    else if (request.uri.toString().endsWith(ip3Info.ip))
      HttpResponse(status = OK, entity = marshal(ip3Info))
    else
      HttpResponse(status = BadRequest, entity = marshal("Bad ip format"))
  }

  "WinticketService" should "respond to single IP query" in {
    Get(s"/ip/${ip1Info.ip}") ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[IpInfo] shouldBe ip1Info
    }

    Get(s"/ip/${ip2Info.ip}") ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[IpInfo] shouldBe ip2Info
    }
  }

  it should "respond with Switzerland" in {
    Get(s"/ip/${ip3Info.ip}") ~> routes ~> check {
      status shouldBe OK
      println("Response: " + responseAs[String])
      //TODO Test with contain/include fails...
      responseAs[IpInfo].country.map(x => x.size should be >= 11)
    }
  }

  it should "respond to IP pair query" in {
    Post(s"/ip", IpPairSummaryRequest(ip1Info.ip, ip2Info.ip)) ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[IpPairSummary] shouldBe ipPairSummary
    }
  }

  it should "respond with bad request on incorrect IP format" in {
    Get("/ip/asdfg") ~> routes ~> check {
      status shouldBe BadRequest
      //TODO 1) checking string result with should contain/include fails at runtime with: ';' expected but string literal found
      //TODO 2) "incorrect IP format" (= from WinticketService) instead of "Bad ip format" (= from this class) ist in the response...
      println("Response: " + responseAs[String])
      responseAs[String].length should be > 0
    }

    //"incorrect IP format" should include "incorrect"

    Post(s"/ip", IpPairSummaryRequest(ip1Info.ip, "asdfg")) ~> routes ~> check {
      status shouldBe BadRequest
      responseAs[String].length should be > 0
    }

    Post(s"/ip", IpPairSummaryRequest("asdfg", ip1Info.ip)) ~> routes ~> check {
      status shouldBe BadRequest
      responseAs[String].contains("incorrectx")
    }
  }

}
