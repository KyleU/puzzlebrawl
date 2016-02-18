package models.audit

import enumeratum._

sealed abstract class DailyMetric(val title: String) extends EnumEntry

object DailyMetric extends Enum[DailyMetric] {
  override val values = findValues
  case object BrawlsStarted extends DailyMetric("Brawls")
  case object BrawlsWon extends DailyMetric("Won")
  case object BrawlsAdandoned extends DailyMetric("Abandoned")
  case object Signups extends DailyMetric("Signups")
  case object Requests extends DailyMetric("Requests")
  case object Feedbacks extends DailyMetric("Feedbacks")
  case object ServerFreeSpace extends DailyMetric("Server Free Space")
  case object ReportSent extends DailyMetric("Mailed")
}

