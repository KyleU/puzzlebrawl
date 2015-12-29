scalacOptions ++= Seq( "-unchecked", "-deprecation" )

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.6")

// SBT-Web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.7")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

// Scala.js
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.5")

addSbtPlugin("com.vmunier" % "sbt-play-scalajs" % "0.2.8")

// Benchmarking
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.5")

addSbtPlugin("io.gatling" % "gatling-sbt" % "2.1.7")

// Source Control
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.4")

// Code Quality
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.0") // dependencyGraph

addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "0.1.1")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.8") // dependencyUpdates

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0") // scalariformFormat

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.3")

addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.5") // stats

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0") // scalastyle

addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.0")
