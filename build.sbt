import com.typesafe.sbt.SbtMultiJvm.multiJvmSettings
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm


val akkaVersion = "2.5.3"

lazy val gatlingCluster = project
  .in(file("."))
  .settings(multiJvmSettings: _*)
  .settings(
    version := "1.0",
    name := "gatling-cluster",
    scalaVersion := "2.11.11",
    libraryDependencies ++= Seq(
      "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.5",
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe" % "config" % "1.3.1",
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
      "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    ),
    parallelExecution in Test := false,
    assemblyMergeStrategy in assembly := {
      case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    mainClass in assembly := Some("de.codecentric.gatling.cluster.Main")
  )
  .configs(MultiJvm)


