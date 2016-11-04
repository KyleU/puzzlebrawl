import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.typesafe.sbt.{ GitBranchPrompt, GitVersioning }
import com.typesafe.sbt.SbtScalariform.{ ScalariformKeys, scalariformSettings }
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import playscalajs.ScalaJSPlay
import sbt.Keys._
import sbt._

object Client {

  private[this] val clientSettings = Seq(
    scalaVersion := Shared.Versions.scala,
    persistLauncher := false,
    //noinspection ScalaDeprecation
    unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % Dependencies.ScalaJS.domVersion,
      "com.lihaoyi" %%% "upickle" % Dependencies.ScalaJS.uPickleVersion
    ),
    scalaJSStage in Global := FastOptStage,
    scapegoatIgnoredFiles := Seq(".*/json/.*"),
    scapegoatVersion := Dependencies.scapegoatVersion,
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
  )

  lazy val client = (project in file("client"))
    .enablePlugins(GitVersioning, GitBranchPrompt)
    .settings(scalariformSettings: _*)
    .enablePlugins(ScalaJSPlugin, ScalaJSPlay)
    .settings(clientSettings: _*)
    .dependsOn(Shared.sharedJs)
}
