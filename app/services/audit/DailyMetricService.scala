package services.audit

import models.audit.{ DailyMetric, DailyMetricResult }
import models.audit.DailyMetric._
import models.queries.audit.DailyMetricQueries
import org.joda.time.LocalDate
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database
import utils.DateUtils

import scala.concurrent.Future
import scala.util.control.NonFatal

object DailyMetricService {
  def getMetric(d: LocalDate, m: DailyMetric) = Database.query(DailyMetricQueries.GetValue(d, m))

  def getMetrics(d: LocalDate) = Database.query(DailyMetricQueries.GetMetrics(d)).flatMap { m =>
    if (m.size == DailyMetric.values.size) {
      Future.successful((d, (m, 0)))
    } else {
      val missingMetrics = DailyMetric.values.filterNot(m.keySet.contains)
      calculateMetrics(d, missingMetrics).map { metrics =>
        val models = metrics.map(x => DailyMetricResult(d, x._1, x._2, DateUtils.now)).toSeq
        Database.execute(DailyMetricQueries.insertBatch(models))
        (d, (m ++ metrics, metrics.size))
      }
    }
  }

  def getAllMetrics = Database.query(DailyMetricQueries.GetAllMetrics)

  def setMetric(d: LocalDate, metric: DailyMetric, value: Long) = {
    val dm = DailyMetricResult(d, metric, value, DateUtils.now)
    Database.execute(DailyMetricQueries.UpdateMetric(dm)).flatMap { rowsAffected =>
      if (rowsAffected == 1) {
        Future.successful(dm)
      } else {
        Database.execute(DailyMetricQueries.insert(dm)).map(x => dm)
      }
    }
  }

  def getTotals(last: LocalDate) = Database.query(DailyMetricQueries.GetTotals(last))

  def recalculateMetrics(d: LocalDate) = {
    Database.execute(DailyMetricQueries.RemoveByDay(d)).flatMap { rowsDeleted =>
      getMetrics(d)
    }
  }

  private[this] def calculateMetrics(d: LocalDate, metrics: Seq[DailyMetric]) = {
    val futures = metrics.map { metric =>
      getSql(metric) match {
        case Some(sql) => Database.query(DailyMetricQueries.CalculateMetric(metric, sql, d))
        case None => metric match {
          case DailyMetric.ServerFreeSpace => Future.successful(metric -> getFreeSpace)
          case _ => Future.successful(metric -> 0L)
        }
      }
    }
    Future.sequence(futures).map(_.toMap)
  }

  private[this] def getSql(metric: DailyMetric) = metric match {
    case BrawlsStarted => Some("select count(*) as c from brawls where created >= ? and created < ?")
    case BrawlsWon => Some("select count(*) as c from brawls where created >= ? and created < ? and status = 'win'")
    case BrawlsAdandoned => Some("select count(*) as c from brawls where created >= ? and created < ? and status = 'abandoned'")
    case Signups => Some("select count(*) as c from users where created >= ? and created < ?")
    case Requests => Some("select count(*) as c from requests where started >= ? and started < ?")
    case Feedbacks => Some("select count(*) as c from user_feedback where occurred >= ? and occurred < ?")
    case _ => None
  }

  private[this] def getFreeSpace = try {
    import scala.sys.process._
    val result = "df /".!!
    val lastLine = result.split("\n").last
    val endIndex = lastLine.indexOf('%')
    val startIndex = lastLine.indexOf(' ', endIndex - 5)
    val ret = 100 - lastLine.substring(startIndex, endIndex).trim.toLong
    ret
  } catch {
    case NonFatal(x) => 0L
  }
}
