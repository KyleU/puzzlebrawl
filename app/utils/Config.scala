package utils

import java.net.InetAddress

import _root_.play.api.Play
import com.github.mauricio.async.db.Configuration

object Config {
  val projectId = "puzzlebrawl"
  val projectName = "Puzzle Brawl"
  val version = "0.1"
  val hostname = InetAddress.getLocalHost.getHostName
  val pageSize = 100
}

@javax.inject.Singleton
class Config @javax.inject.Inject() (val cnf: play.api.Configuration) {
  val debug = !Play.isProd(Play.current)

  val fileCacheDir = cnf.getString("cache.dir").getOrElse("./cache")

  // Database
  val databaseConfiguration = new Configuration(
    host = cnf.getString("db.host").getOrElse("127.0.0.1"),
    port = 5432,
    database = cnf.getString("db.database"),
    username = cnf.getString("db.username").getOrElse(Config.projectId),
    password = cnf.getString("db.password")
  )

  // Admin
  val adminEmail = cnf.getString("admin.email").getOrElse(throw new IllegalStateException("Missing admin email."))

  // Notifications
  val slackEnabled = cnf.getBoolean("slack.enabled").getOrElse(false)
  val slackUrl = cnf.getString("slack.url").getOrElse("no_url_provided")

  // Metrics
  val jmxEnabled = cnf.getBoolean("metrics.jmx.enabled").getOrElse(true)
  val graphiteEnabled = cnf.getBoolean("metrics.graphite.enabled").getOrElse(false)
  val graphiteServer = cnf.getString("metrics.graphite.server").getOrElse("127.0.0.1")
  val graphitePort = cnf.getInt("metrics.graphite.port").getOrElse(2003)
  val servletEnabled = cnf.getBoolean("metrics.servlet.enabled").getOrElse(true)
  val servletPort = cnf.getInt("metrics.servlet.port").getOrElse(9001)
}
