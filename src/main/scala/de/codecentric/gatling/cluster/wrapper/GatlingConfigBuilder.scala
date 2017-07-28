package de.codecentric.gatling.cluster.wrapper

import java.util.Collections

import io.gatling.core.ConfigKeys
import io.gatling.core.stats.writer.FileDataWriterType

import scala.collection.mutable

/**
  * Created by ronny on 28.07.17.
  */

class GatlingConfigBuilder {

  def withReportsOnly(): GatlingConfigBuilder = {
    config.put(ConfigKeys.core.directory.ReportsOnly, "collect")
    this
  }


  private val config: mutable.Map[String, Any] = mutable.Map()

  def withNoReports(): GatlingConfigBuilder = {
    config.put(ConfigKeys.charting.NoReports, true)
    this
  }

  def withSimulationClass(clazz: String): GatlingConfigBuilder = {
    config.put(ConfigKeys.core.SimulationClass, clazz)
    this
  }

  def withDataWriter(writer: String): GatlingConfigBuilder = {
    config.put(ConfigKeys.data.Writers, Collections.singleton(FileDataWriterType.name))
    this
  }

  def build(): mutable.Map[String, Any] = config

}

object GatlingConfigBuilder {

  def config(): GatlingConfigBuilder = new GatlingConfigBuilder

}
