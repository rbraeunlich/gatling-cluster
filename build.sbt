name := "gatling-cluster"

version := "1.0"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.5",
  "com.typesafe.akka" %% "akka-actor" % "2.5.3",
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.3" % Test
)

assemblyMergeStrategy in assembly := {
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

mainClass in assembly := Some("de.codecentric.gatling.cluster.Main")