package de.codecentric.gatling.cluster

import java.io.File

import scala.io.Source

/**
  * Created by ronny on 28.07.17.
  */
trait SimulationLogReader {

  def readLogFile(): String = {
    val resultsDirectory = new File("results")
    val newestDirectory = resultsDirectory.listFiles().maxBy(_.lastModified())
    val simulationLog = newestDirectory.listFiles().head
    Source.fromFile(simulationLog, "UTF-8").mkString
  }
}
