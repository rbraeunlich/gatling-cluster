package de.codecentric.gatling.cluster.state

import akka.actor.{Actor, ActorLogging}
import akka.cluster.ClusterEvent._
import akka.cluster.{Cluster, MemberStatus}

class ClusterDomainEventListener extends Actor
  with ActorLogging {
  Cluster(context.system).subscribe(self, classOf[ClusterDomainEvent])

  def receive: PartialFunction[Any, Unit] = {
    case MemberUp(member) =>
      log.info(s"$member UP.")
    case MemberExited(member) =>
      log.info(s"$member EXITED.")
    case MemberRemoved(member, previousState) =>
      if (previousState == MemberStatus.Exiting) {
        log.info(s"Member $member Previously gracefully exited, REMOVED.")
      } else {
        log.info(s"$member Previously downed after unreachable, REMOVED.")
      }
    case UnreachableMember(member) =>
      log.info(s"$member UNREACHABLE")
    case ReachableMember(member) =>
      log.info(s"$member REACHABLE")
    case state: CurrentClusterState =>
      log.info(s"Current state of the cluster: $state")
  }

  override def postStop(): Unit = {
    Cluster(context.system).unsubscribe(self)
    super.postStop()
  }
}