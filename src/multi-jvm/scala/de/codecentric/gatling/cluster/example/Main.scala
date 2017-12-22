package de.codecentric.gatling.cluster.example

import de.codecentric.gatling.cluster.SimpleSimulation
import de.codecentric.gatling.cluster.api.MetaSimulation

object Main extends App with MetaSimulation {
  override val numberOfInstances = 1

  startSimulation(classOf[SimpleSimulation])
}
