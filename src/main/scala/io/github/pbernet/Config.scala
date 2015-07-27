package io.github.pbernet

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val servicesConfig = config.getConfig("services")

  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  val telizeHost = servicesConfig.getString("telizeHost")
  val telizePort = servicesConfig.getInt("telizePort")

}
