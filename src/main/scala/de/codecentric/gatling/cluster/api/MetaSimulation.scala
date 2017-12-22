package de.codecentric.gatling.cluster.api

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory
import de.codecentric.gatling.cluster.simulation.Coordinator
import de.codecentric.gatling.cluster.simulation.Coordinator.StartSimulation
import de.codecentric.gatling.cluster.state.ClusterDomainEventListener
import io.gatling.core.Predef.Simulation

trait MetaSimulation {

  val numberOfInstances: Int

  def startSimulation[T <: Simulation](simulationClazz: Class[T]): Unit = {
    val config = ConfigFactory.load("master.conf")
    val system = ActorSystem("gatling-cluster", config)

    println(s"Starting node with roles: ${Cluster(system).selfRoles}")

    if (system.settings.config.getStringList("akka.cluster.roles").contains("master")) {
      Cluster(system).registerOnMemberUp {
        val coordinator = system.actorOf(Props(classOf[Coordinator], numberOfInstances), "coordinator")
        println("Master node is ready.")
        coordinator ! StartSimulation(simulationClazz.getName)
        system.actorOf(Props(new ClusterDomainEventListener), "cluster-listener")
      }
      // TODO shut down upon answers
    }
  }
}