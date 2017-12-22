package de.codecentric.gatling.cluster.simulation

import java.io.{File, PrintWriter}
import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.BroadcastPool
import de.codecentric.gatling.cluster.simulation.Coordinator.{Results, StartSimulation}
import de.codecentric.gatling.cluster.simulation.Worker.{Finished, Go}
import de.codecentric.gatling.cluster.wrapper.{GatlingConfigBuilder, GatlingRunnerWrapper}

/**
  * Created by ronny on 21.07.17.
  */
class Coordinator extends Actor with ActorLogging {

  def createWorkerRouter(): ActorRef = {
    // TODO make number configurable
    context.actorOf(
      ClusterRouterPool(BroadcastPool(1),
        ClusterRouterPoolSettings(
          totalInstances = 1,
          maxInstancesPerNode = 1,
          allowLocalRoutees = false
        )
      ).props(Worker.props), name = "worker-router"
    )
  }

  override def receive: Receive = {
    case StartSimulation(clazz) =>
      log.info(s"Starting simulation $clazz")
      val router = createWorkerRouter()
      Thread.sleep(3000L)
      router ! Go(clazz, sender())

    case Finished(logContent, starter) =>
      log.info(s"Simulation finished with content $logContent")
      new File("results").mkdir()
      new File("results/collect").mkdir()
      val file = new File("results/collect/simulation" + UUID.randomUUID() + ".log")
      val writer = new PrintWriter(file)
      writer.write(logContent)
      writer.flush()
      generateReport()
      // TODO clean up the simulation files
      starter ! Results(logContent)
  }

  def generateReport(): Unit = {
    val configuration = GatlingConfigBuilder.config().withReportsOnly("collect").build()
    new GatlingRunnerWrapper().run(configuration)
  }
}

object Coordinator {

  case class StartSimulation(clazz: String)

  case class Results(log: String)

}