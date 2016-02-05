package models.queries.audit

import java.util.UUID

import models.audit.AnalyticsEvent
import models.database.{ Query, Row }
import models.queries.BaseQueries
import org.joda.time.{ LocalDate, LocalDateTime }
import play.api.libs.json.{ JsObject, Json }

object AnalyticsEventQueries extends BaseQueries[AnalyticsEvent] {
  override protected val tableName = "analytics_events"
  override protected val columns = Seq("id", "event_type", "user_id", "source_address", "data", "created")
  override protected val searchColumns = Seq("id::text", "event_type", "user_id::text", "source_address")

  def getById(id: UUID) = getBySingleId(id)
  val insert = Insert
  def removeById(id: UUID) = RemoveById(Seq(id))
  val search = Search
  def searchCount(q: String, groupBy: Option[String] = None) = new SearchCount(q, groupBy)

  case object GetDateCounts extends Query[Seq[(LocalDate, Int)]] {
    override def sql = s"""
      select date_trunc('day', created) as d, count(*) as c
      from $tableName
      group by date_trunc('day', created)
      order by date_trunc('day', created) desc
    """
    override def reduce(rows: Iterator[Row]) = rows.map(r => r.as[LocalDateTime]("d").toLocalDate -> r.as[Long]("c").toInt).toSeq
  }

  final case class GetByDate(d: LocalDate) extends Query[Iterator[AnalyticsEvent]] {
    override def sql = s"select ${columns.mkString(", ")} from $tableName where created >= ? and created < ?"
    override def values = Seq(d, d.plusDays(1))
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow)
  }

  override protected def fromRow(row: Row) = {
    val id = row.as[UUID]("id")
    val eventType = AnalyticsEvent.EventType.fromString(row.as[String]("event_type"))
    val userId = row.as[UUID]("user_id")
    val sourceAddress = row.asOpt[String]("source_address")
    val data = Json.parse(row.as[String]("data")).as[JsObject]
    val created = row.as[LocalDateTime]("created")
    AnalyticsEvent(id, eventType, userId, sourceAddress, data, created)
  }

  override protected def toDataSeq(ae: AnalyticsEvent) = Seq(ae.id, ae.eventType.id, ae.userId, ae.sourceAddress, Json.prettyPrint(ae.data), ae.created)
}
