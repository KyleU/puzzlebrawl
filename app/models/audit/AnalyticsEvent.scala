package models.audit

import java.util.UUID

import org.joda.time.LocalDateTime
import play.api.libs.json.JsValue

object AnalyticsEvent {
  sealed abstract class EventType(val id: String)

  object EventType {
    case object ClientTrace extends EventType("trace")

    case object Error extends EventType("error")

    case class Unknown(override val id: String) extends EventType(id)

    val all = Seq(ClientTrace, Error)
    def fromString(s: String) = all.find(_.id == s).getOrElse(Unknown(s))
  }
}

case class AnalyticsEvent(
  id: UUID,
  eventType: AnalyticsEvent.EventType,
  userId: UUID,
  sourceAddress: Option[String],
  data: JsValue,
  created: LocalDateTime)
