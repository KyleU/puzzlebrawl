import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.typesafe.sbt.{ GitBranchPrompt, GitVersioning }
import com.typesafe.sbt.SbtScalariform.{ ScalariformKeys, scalariformSettings }
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import playscalajs.ScalaJSPlay
import playscalajs.ScalaJSPlay.autoImport._
import sbt.Keys._
import sbt._

object Client {
  private[this] val clientSettings = Seq(
    scalaVersion := Shared.Versions.scala,
    persistLauncher := false,
    sourceMapsDirectories += Shared.sharedJs.base / "..",
    unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.2",
      "com.lihaoyi" %%% "upickle" % "0.3.6"
    ),
    scalaJSStage in Global := FastOptStage,
    scapegoatIgnoredFiles := Seq(".*/json/.*"),
    scapegoatVersion := "1.1.0",
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
  )

  lazy val client = (project in file("client"))
    .enablePlugins(GitVersioning)
    .enablePlugins(GitBranchPrompt)
    .settings(scalariformSettings: _*)
    .enablePlugins(ScalaJSPlugin, ScalaJSPlay)
    .settings(clientSettings: _*)
    .dependsOn(Shared.sharedJs)
}
