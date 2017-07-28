package de.codecentric.gatling.cluster

import java.io.{File, PrintWriter}
import java.util.{Collections, UUID}

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.BroadcastPool
import de.codecentric.gatling.cluster.SimulationCoordinator.{Results, StartSimulation}
import de.codecentric.gatling.cluster.SimulationWorker.{Finished, Go}
import io.gatling.app.Gatling
import io.gatling.core.ConfigKeys
import io.gatling.core.stats.writer.FileDataWriterType

import scala.collection.mutable
import scala.concurrent.duration._

/**
  * Created by ronny on 21.07.17.
  */
class SimulationCoordinator extends Actor with ActorLogging {

  def createWorkerRouter(): ActorRef = {
    // TODO make number configurable
    context.actorOf(
      ClusterRouterPool(BroadcastPool(1),
        ClusterRouterPoolSettings(
          totalInstances = 1,
          maxInstancesPerNode = 1,
          allowLocalRoutees = false,
          useRole = None
        )
      ).props(Props[SimulationWorker]), name = "worker-router"
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
      starter ! Results(logContent)
  }

  def generateReport() = {
    val configuration = mutable.Map(ConfigKeys.core.directory.ReportsOnly -> "collect")
    Gatling.fromMap(configuration)
  }
}

object SimulationCoordinator {

  case class StartSimulation(clazz: String)

  case class Results(log: String)

}