import sbt._

object Dependencies {
  val scapegoatVersion = "1.3.0"

  object Cache {
    val ehCache = "net.sf.ehcache" % "ehcache-core" % "2.6.11"
  }

  object Database {
    val postgresAsync = "com.github.mauricio" %% "postgresql-async" % "0.2.20"
  }

  object Akka {
    private[this] val version = "2.4.14"
    val actor = "com.typesafe.akka" %% "akka-actor" % version
    val remote = "com.typesafe.akka" %% "akka-remote" % version
    val logging = "com.typesafe.akka" %% "akka-slf4j" % version
    val cluster = "com.typesafe.akka" %% "akka-cluster" % version
    val clusterMetrics = "com.typesafe.akka" %% "akka-cluster-metrics" % version
    val clusterTools = "com.typesafe.akka" %% "akka-cluster-tools" % version
    val testkit = "com.typesafe.akka" %% "akka-testkit" % version
  }

  object Play {
    val version = "2.4.8"
    val playFilters = play.sbt.PlayImport.filters
    val playWs = play.sbt.PlayImport.ws
    val playJson = play.sbt.PlayImport.json
    val playTest = "com.typesafe.play" %% "play-test" % version
  }

  object WebJars {
    val requireJs = "org.webjars" % "requirejs" % "2.3.2"
    val bootstrap = "org.webjars" % "bootstrap" % "3.3.7"
    val underscore = "org.webjars" % "underscorejs" % "1.8.3"
    val d3 = "org.webjars" % "d3js" % "3.5.17"
    val nvd3 = "org.webjars" % "nvd3-community" % "1.7.0"
  }

  object Mail {
    val mailer = "com.typesafe.play" %% "play-mailer" % "3.0.1"
  }

  object Authentication {
    val silhouette = "com.mohiva" %% "play-silhouette" % "3.0.5"
  }

  object ScalaJS {
    val domVersion = "0.9.1"
    val uPickleVersion = "0.4.4"
  }

  object Metrics {
    private[this] val version = "3.1.2"
    val jvm = "io.dropwizard.metrics" % "metrics-jvm" % version withSources()
    val ehcache = "io.dropwizard.metrics" % "metrics-ehcache" % version withSources() intransitive()
    val healthChecks = "io.dropwizard.metrics" % "metrics-healthchecks" % version withSources() intransitive()
    val json = "io.dropwizard.metrics" % "metrics-json" % version withSources()
    val servlets = "io.dropwizard.metrics" % "metrics-servlets" % version withSources() intransitive()
    val graphite = "io.dropwizard.metrics" % "metrics-graphite" % version withSources() intransitive()
    val metrics = "nl.grons" %% "metrics-scala" % "3.5.5" withSources()
    val jettyServlet = "org.eclipse.jetty" % "jetty-servlet" % "9.3.14.v20161028" withSources()
  }

  object Miscellaneous {
    val lanterna = "com.googlecode.lanterna" % "lanterna" % "3.0.0-beta3"
    val enumeratum = "com.beachape" %% "enumeratum-play-json" % "1.3.7"
  }

  object Testing {
    val gatlingVersion = "2.2.3"
    val gatlingCore = "io.gatling" % "gatling-test-framework" % gatlingVersion % "test"
    val gatlingCharts = "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "test"
  }
}
