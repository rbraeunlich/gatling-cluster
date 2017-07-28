package de.codecentric.gatling.cluster.wrapper

import io.gatling.app.Gatling

import scala.collection.mutable

/**
  * Created by ronny on 28.07.17.
  */
class GatlingRunnerWrapper extends Serializable{

  def run(config: mutable.Map[String, _]): Int = Gatling.fromMap(config)

}
