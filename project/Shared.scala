import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.typesafe.sbt.{ GitBranchPrompt, GitVersioning }
import sbt._
import sbt.Keys._

import com.typesafe.sbt.SbtScalariform.{ScalariformKeys, scalariformSettings}

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

import playscalajs.ScalaJSPlay
import playscalajs.ScalaJSPlay.autoImport._

object Shared {
  val projectId = "puzzlebrawl"

  val compileOptions = Seq(
    "-encoding", "UTF-8", "-feature", "-deprecation", "-unchecked", "–Xcheck-null", "-Xfatal-warnings", "-Xlint",
    "-Ywarn-adapted-args", "-Ywarn-dead-code", "-Ywarn-inaccessible", "-Ywarn-nullary-override", "-Ywarn-numeric-widen"
  )

  object Versions {
    val app = "0.1-SNAPSHOT"
    val scala = "2.11.7"
  }

  lazy val sharedJs = (crossProject.crossType(CrossType.Pure) in file("shared"))
    .settings(scalaVersion := Versions.scala)
    .enablePlugins(ScalaJSPlay)
    .settings(
      sourceMapsBase := baseDirectory.value / "..",
      scalaJSStage in Global := FastOptStage,
      scapegoatIgnoredFiles := Seq(".*"),
      scapegoatVersion := Dependencies.scapegoatVersion
  ).js

  lazy val sharedJvm = (project in file("shared"))
    .enablePlugins(GitVersioning)
    .enablePlugins(GitBranchPrompt)
    .settings(scalariformSettings: _*)
    .settings(
      scalaVersion := Versions.scala,
      scapegoatVersion := Dependencies.scapegoatVersion,
      ScalariformKeys.preferences := ScalariformKeys.preferences.value
    )
}
