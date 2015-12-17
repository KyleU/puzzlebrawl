import com.typesafe.sbt.GitVersioning
import io.gatling.sbt.GatlingPlugin
import pl.project13.scala.sbt.JmhPlugin
import sbt.Keys._
import sbt._

object Benchmarking {
  private[this] val dependencies = Seq(
    Dependencies.Testing.gatlingCore,
    Dependencies.Testing.gatlingCharts
  )

  private[this] lazy val benchmarkingSettings = Seq(
    name := "benchmarking",
    version := Shared.Versions.app,
    scalaVersion := Shared.Versions.scala,

    scalacOptions ++= Shared.compileOptions,
    scalacOptions in Test ++= Seq("-Yrangepos"),

    libraryDependencies ++= dependencies
  )

  lazy val benchmarking = (project in file("benchmarking"))
    .enablePlugins(GitVersioning)
    .enablePlugins(GatlingPlugin)
    .enablePlugins(JmhPlugin)
    .settings(benchmarkingSettings: _*)
    .dependsOn(Shared.sharedJvm)
}
