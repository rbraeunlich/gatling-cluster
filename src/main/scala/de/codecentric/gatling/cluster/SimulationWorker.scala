package de.codecentric.gatling.cluster

import java.io.File
import java.util.Collections

import akka.actor.{Actor, ActorLogging, ActorRef}
import de.codecentric.gatling.cluster.SimulationWorker.{Finished, Go}
import io.gatling.app.Gatling
import io.gatling.core.ConfigKeys
import io.gatling.core.stats.writer.FileDataWriterType

import scala.collection.mutable
import scala.io.Source

/**
  * Created by ronny on 21.07.17.
  */
class SimulationWorker extends Actor with ActorLogging {
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
      val configuration = mutable.Map(
        ConfigKeys.charting.NoReports -> true,
        ConfigKeys.core.SimulationClass -> clazz,
        ConfigKeys.data.Writers -> Collections.singleton(FileDataWriterType.name))
      Gatling.fromMap(configuration)
      log.info(s"Finished simulation $clazz")
      sender() ! Finished(readLogFile(), starter)
  }
}

object SimulationWorker {

  case class Go(clazz: String, starter: ActorRef)

  case class Finished(logContent: String, starter: ActorRef)

}
