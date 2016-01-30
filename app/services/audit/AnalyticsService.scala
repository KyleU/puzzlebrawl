package services.audit

import java.util.UUID

import models.audit.AnalyticsEvent
import models.audit.AnalyticsEvent.EventType
import models.queries.audit.AnalyticsEventQueries
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsValue
import services.database.Database
import utils.DateUtils

object AnalyticsService {
  def error(userId: UUID, sourceAddress: String, data: JsValue) = log(EventType.Error, userId, sourceAddress, data)
  def trace(userId: UUID, sourceAddress: String, data: JsValue) = log(EventType.ClientTrace, userId, sourceAddress, data)

  private[this] def log(eventType: AnalyticsEvent.EventType, userId: UUID, sourceAddress: String, data: JsValue) = {
    val event = AnalyticsEvent(
      id = UUID.randomUUID,
      eventType = eventType,
      userId = userId,
      sourceAddress = Some(sourceAddress),
      data = data,
      created = DateUtils.now
    )
    Database.execute(AnalyticsEventQueries.insert(event)).map(x => event)
  }

  def searchEvents(q: String, orderBy: String, page: Int) = for {
    count <- Database.query(AnalyticsEventQueries.searchCount(q))
    list <- Database.query(AnalyticsEventQueries.search(q, orderBy, Some(page)))
  } yield count -> list

  def remove(id: UUID) = Database.execute(AnalyticsEventQueries.removeById(id))
}
