scalacOptions ++= Seq( "-unchecked", "-deprecation" )

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.8")

// SBT-Web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.7")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

// Scala.js
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.13")

addSbtPlugin("com.vmunier" % "sbt-play-scalajs" % "0.3.0" exclude("org.scala-js", "sbt-scalajs"))

// Benchmarking
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.17")

addSbtPlugin("io.gatling" % "gatling-sbt" % "2.2.1")

// Source Control
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.4")

// Dependency Resolution
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M14-3")

// App Packaging
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.5")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

// Code Quality
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.0") // dependencyGraph

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.0") // dependencyUpdates

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0") // scalariformFormat

addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.5") // stats

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.4")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0") // scalastyle

addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.14")

// Documentation
addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.1")
