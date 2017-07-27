package de.codecentric.gatling.cluster

import akka.actor.{Actor, ActorLogging}
import de.codecentric.gatling.cluster.SimulationWorker.Go
import io.gatling.app.Gatling

/**
  * Created by ronny on 21.07.17.
  */
class SimulationWorker extends Actor with ActorLogging {
  val SIMULATION_CLASS_OPTION = "-s"

  val NO_REPORTS_OPTION = "-nr"

  override def receive: Receive = {
    case Go(clazz) =>
      log.info(s"Starting simulation $clazz")
      Gatling.main(Array(NO_REPORTS_OPTION, SIMULATION_CLASS_OPTION, clazz))
      log.info(s"Finished simulation $clazz")
  }
}

object SimulationWorker {

  case class Go(clazz: String)

}
