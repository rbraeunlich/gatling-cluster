package de.codecentric.gatling.cluster.simulation

import java.util.Collections

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import de.codecentric.gatling.cluster.StopSystemAfterAll
import de.codecentric.gatling.cluster.simulation.Worker.{Finished, Go}
import de.codecentric.gatling.cluster.wrapper.GatlingRunnerWrapper
import io.gatling.core.ConfigKeys
import io.gatling.core.stats.writer.FileDataWriterType
import org.scalatest.{Matchers, WordSpecLike}

import scala.collection.mutable

/**
  * Created by ronny on 28.07.17.
  */
class WorkerSpec extends TestKit(ActorSystem("WorkerSpec"))
  with WordSpecLike
  with Matchers
  with StopSystemAfterAll
  with ImplicitSender {

  "SimulationWorker" must {
    "start Gatling upon Go message" in {
      val wrapper = new NullGatlingRunnerWrapper
      val actor = system.actorOf(Props(new Worker(wrapper) with NullSimulationLogReader))

      actor ! Go("nonExistingClazz", self)

      expectMsgClass(classOf[Finished])
      wrapper.ran shouldBe true
    }
    "create the correct configuration" in {
      val wrapper = new NullGatlingRunnerWrapper
      val actor = system.actorOf(Props(new Worker(wrapper) with NullSimulationLogReader))
      val clazz = "nonExistingClazz"

      actor ! Go(clazz, self)

      expectMsgClass(classOf[Finished])
      wrapper.config should equal(Map(
        ConfigKeys.charting.NoReports -> true,
        ConfigKeys.core.SimulationClass -> clazz,
        ConfigKeys.data.Writers -> Collections.singleton(FileDataWriterType.name)))
    }
    "send a Finished message containing the log and a reference to the initiator" in {
      val wrapper = new NullGatlingRunnerWrapper
      val actor = system.actorOf(Props(new Worker(wrapper) with NullSimulationLogReader))
      val clazz = "nonExistingClazz"

      actor ! Go(clazz, self)

      expectMsg(Finished("null", self))
    }
  }

  private class NullGatlingRunnerWrapper extends GatlingRunnerWrapper {
    var config: mutable.Map[String, _] = _
    var ran: Boolean = false

    override def run(config: mutable.Map[String, _]): Int = {
      this.config = config
      this.ran = true
      0
    }
  }

  private trait NullSimulationLogReader {
    this: GatlingSimulationLogReader =>

    override def readLogFile(): String = "null"
  }

}
