package io.github.pbernet

import akka.event.{ NoLogging, LoggingAdapter }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{ WordSpec, Matchers }

//TODO Find common stuff for all Tests
trait ServiceTestBase extends WordSpec with Matchers with ScalatestRouteTest {
  protected def log: LoggingAdapter = NoLogging
}
