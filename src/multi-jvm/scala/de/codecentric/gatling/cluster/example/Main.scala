package de.codecentric.gatling.cluster.example

import de.codecentric.gatling.cluster.SimpleSimulation
import de.codecentric.gatling.cluster.api.MetaSimulation

object Main extends App with MetaSimulation {
  // TODO set the min-nr of worker to the same value
  override val numberOfInstances = 2

  startSimulation(classOf[SimpleSimulation])
}
