package io.github.pbernet

import akka.http.scaladsl.server.Directives._

trait Service extends BaseService {
  val routes = get {
    log.info("/ executed")
    complete("hello world")
  }
}
