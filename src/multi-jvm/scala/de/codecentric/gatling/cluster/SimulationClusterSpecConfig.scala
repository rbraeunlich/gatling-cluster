package de.codecentric.gatling.cluster

import akka.remote.testkit.MultiNodeConfig
import com.typesafe.config.ConfigFactory

/**
  * Created by ronny on 24.07.17.
  */
object SimulationClusterSpecConfig extends MultiNodeConfig {

  val seed = role("seed")
  val coordinator = role("coordinator")
  val worker = role("worker")

  commonConfig(ConfigFactory.parseResources("testCluster.conf"))
}
