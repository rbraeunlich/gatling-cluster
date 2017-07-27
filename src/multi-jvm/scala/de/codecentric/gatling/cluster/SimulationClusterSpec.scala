package de.codecentric.gatling.cluster

import akka.actor.{Address, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{CurrentClusterState, MemberUp}
import akka.remote.testkit.{MultiNodeSpec, MultiNodeSpecCallbacks}
import akka.testkit.ImplicitSender
import de.codecentric.gatling.cluster.SimulationCoordinator.StartSimulation
import de.codecentric.gatling.cluster.SimulationWorker.Finished
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

import scala.concurrent.duration._

/**
  * Created by ronny on 24.07.17.
  */

class SimulationClusterSpecMultiJvmNode1 extends SimulationClusterSpec

class SimulationClusterSpecMultiJvmNode2 extends SimulationClusterSpec

class SimulationClusterSpecMultiJvmNode3 extends SimulationClusterSpec

class SimulationClusterSpec extends MultiNodeSpec(SimulationClusterSpecConfig)
  with STMultiNodeSpec with ImplicitSender {

  import SimulationClusterSpecConfig._

  override def initialParticipants: Int = roles.size

  val seedAddress: Address = node(seed).address
  val coordinatorAddress: Address = node(coordinator).address
  val workerAddress: Address = node(worker).address

  "A simulation cluster" must {
    "form the cluster" in within(10.seconds) {
      Cluster(system).subscribe(testActor, classOf[MemberUp])
      expectMsgClass(classOf[CurrentClusterState])

      Cluster(system).join(seedAddress)

      receiveN(3).map { case MemberUp(m) => m.address }.toSet should be(
        Set(seedAddress, coordinatorAddress, workerAddress)
      )

      Cluster(system).unsubscribe(testActor)
      enterBarrier("cluster-up")
    }

    "execute a simple simulation" in within(20.seconds) {
      runOn(coordinator) {
        val coordinator = system.actorOf(Props[SimulationCoordinator], "coordinator")
        coordinator ! StartSimulation("de.codecentric.gatling.cluster.SimpleSimulation")
        expectMsg(10.seconds, Finished)
      }
      enterBarrier("simulation-done")
    }
  }
}
