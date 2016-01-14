import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.typesafe.sbt.GitVersioning
import com.typesafe.sbt.SbtScalariform._
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

    libraryDependencies ++= dependencies,

    scapegoatVersion := Dependencies.scapegoatVersion,
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
  )

  lazy val benchmarking = (project in file("benchmarking"))
    .enablePlugins(GitVersioning)
    .enablePlugins(play.sbt.PlayScala)
    .settings(scalariformSettings: _*)
    .enablePlugins(GitVersioning)
    .enablePlugins(GatlingPlugin)
    .enablePlugins(JmhPlugin)
    .settings(benchmarkingSettings: _*)
    .dependsOn(Shared.sharedJvm)
}
