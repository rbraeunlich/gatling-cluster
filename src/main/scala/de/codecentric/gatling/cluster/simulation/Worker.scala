package de.codecentric.gatling.cluster.simulation

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import de.codecentric.gatling.cluster.simulation.Worker.{Finished, Go}
import de.codecentric.gatling.cluster.wrapper.{GatlingConfigBuilder, GatlingRunnerWrapper}

/**
  * Created by ronny on 21.07.17.
  */
class Worker(wrapper: GatlingRunnerWrapper) extends Actor with ActorLogging with GatlingSimulationLogReader {

  val SIMULATION_CLASS_OPTION = "-s"

  val NO_REPORTS_OPTION = "-nr"

  override def receive: Receive = {
    case Go(clazz, starter) =>
      log.info(s"Starting simulation $clazz")
      val configuration = GatlingConfigBuilder.config()
        .withNoReports()
        .withFileDataWriter()
        .withSimulationClass(clazz)
        .build()
      wrapper.run(configuration)
      log.info(s"Finished simulation $clazz")
      sender() ! Finished(readLogFile(), starter)
  }
}

object Worker {

  case class Go(clazz: String, starter: ActorRef)

  case class Finished(logContent: String, starter: ActorRef)

  def props: Props = Props(classOf[Worker], new GatlingRunnerWrapper)

}
