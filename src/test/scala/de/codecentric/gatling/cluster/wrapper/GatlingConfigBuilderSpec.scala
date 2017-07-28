package de.codecentric.gatling.cluster.wrapper

import java.util.Collections

import io.gatling.core.ConfigKeys
import io.gatling.core.stats.writer.FileDataWriterType
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ronny on 28.07.17.
  */
class GatlingConfigBuilderSpec extends WordSpec with Matchers {

  "GatlingConfigBuilder" must {
    "place the class name into the properties" in {
      val clazz = "foo.Bar"
      val config = GatlingConfigBuilder.config().withSimulationClass(clazz).build()

      config should contain(ConfigKeys.core.SimulationClass -> clazz)
    }

    "set the no reports value to true" in {
      val config = GatlingConfigBuilder.config().withNoReports().build()

      config should contain(ConfigKeys.charting.NoReports -> true)
    }

    "set the reports only value to the given directory" in {
      val dir = "dir"
      val config = GatlingConfigBuilder.config().withReportsOnly(dir).build()

      config should contain(ConfigKeys.core.directory.ReportsOnly -> dir)
    }

    "set the FileDataWriter as writer within a set" in {
      val config = GatlingConfigBuilder.config().withFileDataWriter().build()

      config should contain(ConfigKeys.data.Writers -> Collections.singleton(FileDataWriterType.name))
    }
  }
}
