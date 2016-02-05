import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.typesafe.sbt.GitVersioning
import com.typesafe.sbt.SbtScalariform.{ ScalariformKeys, scalariformSettings }
import com.typesafe.sbt.digest.Import._
import com.typesafe.sbt.gzip.Import._
import com.typesafe.sbt.jse.JsEngineImport.JsEngineKeys
import com.typesafe.sbt.jshint.Import.JshintKeys
import com.typesafe.sbt.less.Import._
import com.typesafe.sbt.rjs.Import._
import com.typesafe.sbt.web.Import._
import com.typesafe.sbt.web.SbtWeb
import play.routes.compiler.InjectedRoutesGenerator
import play.sbt.routes.RoutesKeys.routesGenerator
import playscalajs.PlayScalaJS.autoImport._
import sbt.Keys._
import sbt.Project.projectToRef
import sbt._
import sbtide.Keys._
import tut.Plugin._
import wartremover.WartRemover.autoImport._

object Server {
  private[this] val dependencies = {
    import Dependencies._
    Seq(
      Cache.ehCache, Database.postgresAsync, Mail.mailer, Miscellaneous.lanterna,
      Akka.actor, Akka.logging, Play.playFilters, Play.playWs, Play.playTest, Authentication.silhouette,
      Metrics.metrics, Metrics.healthChecks, Metrics.json, Metrics.jvm, Metrics.ehcache, Metrics.jettyServlet, Metrics.servlets, Metrics.graphite,
      WebJars.requireJs, WebJars.bootstrap, WebJars.underscore, WebJars.d3, WebJars.nvd3,
      Testing.akkaTestkit, Testing.gatlingCore, Testing.gatlingCharts
    )
  }

  private[this] lazy val serverSettings = Seq(
    name := Shared.projectId,
    version := Shared.Versions.app,
    scalaVersion := Shared.Versions.scala,

    scalacOptions ++= Shared.compileOptions,
    scalacOptions in Test ++= Seq("-Yrangepos"),

    resolvers += Resolver.jcenterRepo,
    libraryDependencies ++= dependencies,

    routesGenerator := InjectedRoutesGenerator,

    scalaJSProjects := Seq(Client.client),

    // Sbt-Web
    JsEngineKeys.engineType := JsEngineKeys.EngineType.Node,
    pipelineStages := Seq(scalaJSProd, rjs, digest, gzip),
    includeFilter in (Assets, LessKeys.less) := "*.less",
    excludeFilter in (Assets, LessKeys.less) := "_*.less",
    LessKeys.compress in Assets := true,
    JshintKeys.config := Some(new java.io.File("conf/.jshintrc")),

    // Code Quality
    scapegoatIgnoredFiles := Seq(".*/Row.scala", ".*/Routes.scala", ".*/ReverseRoutes.scala", ".*/JavaScriptReverseRoutes.scala", ".*/*.template.scala"),
    scapegoatDisabledInspections := Seq("DuplicateImport"),
    scapegoatVersion := Dependencies.scapegoatVersion,
    ScalariformKeys.preferences := ScalariformKeys.preferences.value,
    wartremoverErrors ++= Shared.includedWartRemovers,
    tutSourceDirectory := baseDirectory.value / "doc",

    // IntelliJ import fixes
    ideExcludedDirectories <<= (baseDirectory, target, crossTarget) { (b, t, ct) =>
      (t * "*").filter(_.isDirectory).filter(_ != ct).get ++
        (ct * "*").filter(_.isDirectory).filter(f => !(f.name.contains("twirl") || f.name.contains("routes"))).get ++
        Seq(b / "logs") ++ Seq(b / "public" / "lib") ++ Seq(b / "offline")
    }
  )

  lazy val server = Project(id = Shared.projectId, base = file("."))
    .enablePlugins(GitVersioning)
    .enablePlugins(SbtWeb)
    .enablePlugins(play.sbt.PlayScala)
    .settings(scalariformSettings: _*)
    .settings(tutSettings: _*)
    .settings(serverSettings: _*)
    .aggregate(projectToRef(Client.client))
    .aggregate(Shared.sharedJvm)
    .dependsOn(Shared.sharedJvm)
}
