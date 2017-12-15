package de.codecentric.gatling.cluster

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory
import de.codecentric.gatling.cluster.simulation.Coordinator
import de.codecentric.gatling.cluster.simulation.Coordinator.StartSimulation
import de.codecentric.gatling.cluster.state.ClusterDomainEventListener

object Main extends App {
  val config = ConfigFactory.load()
  val system = ActorSystem("gatling-cluster", config)

  println(s"Starting node with roles: ${Cluster(system).selfRoles}")

  if(system.settings.config.getStringList("akka.cluster.roles").contains("master")) {
    Cluster(system).registerOnMemberUp {
      val coordinator = system.actorOf(Props[Coordinator], "coordinator")
      println("Master node is ready.")
      //TODO remove the hard coded class
      coordinator ! StartSimulation("de.codecentric.gatling.cluster.SimpleSimulation")
      system.actorOf(Props(new ClusterDomainEventListener), "cluster-listener")
    }
  }


}
