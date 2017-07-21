package de.codecentric.gatling.cluster

import com.typesafe.config.ConfigFactory
import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import de.codecentric.gatling.cluster.SimulationCoordinator.StartSimulation

object Main extends App {
  val config = ConfigFactory.load()
  val system = ActorSystem("gatling-cluster", config)

  println(s"Starting node with roles: ${Cluster(system).selfRoles}")

  if(system.settings.config.getStringList("akka.cluster.roles").contains("master")) {
    Cluster(system).registerOnMemberUp {
      val coordinator = system.actorOf(Props[SimulationCoordinator], "receptionist")
      println("Master node is ready.")
      coordinator ! StartSimulation("de.codecentric.gatling.cluster.SimpleSimulation")
      system.actorOf(Props(new ClusterDomainEventListener), "cluster-listener")
    }
  }


}
