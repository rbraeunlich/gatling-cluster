package de.codecentric.gatling.cluster

import java.io.File

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import de.codecentric.gatling.cluster.SimulationWorker.{Finished, Go}
import de.codecentric.gatling.cluster.wrapper.{GatlingConfigBuilder, GatlingRunnerWrapper}

import scala.io.Source

/**
  * Created by ronny on 21.07.17.
  */
class SimulationWorker(wrapper: GatlingRunnerWrapper) extends Actor with ActorLogging {

  val SIMULATION_CLASS_OPTION = "-s"

  val NO_REPORTS_OPTION = "-nr"

  def readLogFile(): String = {
    val resultsDirectory = new File("results")
    val newestDirectory = resultsDirectory.listFiles().maxBy(_.lastModified())
    val simulationLog = newestDirectory.listFiles().head
    Source.fromFile(simulationLog, "UTF-8").mkString
  }

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

object SimulationWorker {

  case class Go(clazz: String, starter: ActorRef)

  case class Finished(logContent: String, starter: ActorRef)

  def props: Props = Props(classOf[SimulationWorker], new GatlingRunnerWrapper)

}
