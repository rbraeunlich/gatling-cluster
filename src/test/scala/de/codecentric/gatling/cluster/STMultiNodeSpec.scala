package de.codecentric.gatling.cluster

import akka.remote.testkit.MultiNodeSpecCallbacks
import akka.testkit.ImplicitSender
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
  * Hooks up MultiNodeSpec with ScalaTest
  */
trait STMultiNodeSpec extends MultiNodeSpecCallbacks
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def beforeAll() = multiNodeSpecBeforeAll()

  override def afterAll() = multiNodeSpecAfterAll()
}