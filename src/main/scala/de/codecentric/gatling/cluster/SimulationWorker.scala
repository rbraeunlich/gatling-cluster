package de.codecentric.gatling.cluster

import akka.actor.{Actor, ActorLogging}
import de.codecentric.gatling.cluster.SimulationWorker.{Finished, Go}
import io.gatling.app.Gatling
import io.gatling.core.ConfigKeys
import io.gatling.recorder.ConfigOverrides

import scala.collection.mutable

/**
  * Created by ronny on 21.07.17.
  */
class SimulationWorker extends Actor with ActorLogging {
  val SIMULATION_CLASS_OPTION = "-s"

  val NO_REPORTS_OPTION = "-nr"

  override def receive: Receive = {
    case Go(clazz) =>
      log.info(s"Starting simulation $clazz")
      val configuration = mutable.Map(ConfigKeys.charting.NoReports -> true, ConfigKeys.core.SimulationClass -> clazz)
      Gatling.fromMap(configuration)
      log.info(s"Finished simulation $clazz")
      sender() ! Finished
  }
}

object SimulationWorker {

  case class Go(clazz: String)

  case object Finished

}
