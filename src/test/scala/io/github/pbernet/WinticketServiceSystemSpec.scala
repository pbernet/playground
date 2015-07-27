package io.github.pbernet

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.funspec.GatlingHttpFunSpec
import org.scalatest.Matchers

//This class is a Gatling-Simulation and connects directly via HTTP to the REST Interface
//It is started via "sbt test" (using the sbt gatling plugin)
//Normal FlatSpec Tests (= with org.scalatest.FlatSpecLike with Matchers) must not be included in this test
//because the the spec(s) are executed twice...
class WinticketServiceSystemSpec extends GatlingHttpFunSpec {

  //val baseURL = "http://www.telize.com"
  val baseURL = "http://localhost:9000"
  override def httpConf = super.httpConf.header("MyHeader", "MyValue")

  spec {
    println("XXX Starting Test XXX: ")
    http("GeoIPtest")
      .get("/ip/91.199.98.146")
      .check(status.is(200))
      .check(jsonPath("$..country").exists)
      .check(jsonPath("$..country").is("Switzerland"))
  }

  spec {
    http("GeoIPtest2")
      .get("/geoip/91.199.98.146")
      .check(status.is(404))
  }

  //  "DummyTest" should "be always true" in {
  //    11 should be >= 11
  //  }
}

object WinticketServiceSystemSpec {

  def h1 = css("h1")

}
