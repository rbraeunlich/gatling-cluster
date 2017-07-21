package de.codecentric.gatling.cluster

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.BroadcastPool
import de.codecentric.gatling.cluster.SimulationCoordinator.StartSimulation
import de.codecentric.gatling.cluster.SimulationWorker.Go
import scala.concurrent.duration._

/**
  * Created by ronny on 21.07.17.
  */
class SimulationCoordinator extends Actor with ActorLogging {

  def createWorkerRouter(): ActorRef = {
    context.actorOf(
      ClusterRouterPool(BroadcastPool(10),
        ClusterRouterPoolSettings(
          totalInstances = 100,
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
      router ! Go(clazz)
  }
}

object SimulationCoordinator {

  case class StartSimulation(clazz: String)

}