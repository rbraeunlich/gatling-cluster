package de.codecentric.gatling.cluster

import akka.actor.Actor
import de.codecentric.gatling.cluster.SimulationWorker.Go
import io.gatling.app.Gatling

/**
  * Created by ronny on 21.07.17.
  */
class SimulationWorker extends Actor {
  override def receive: Receive = {
    case Go(clazz) => Gatling.main(Array("-s", clazz))
  }
}

object SimulationWorker {

  case class Go(clazz: String)

}
