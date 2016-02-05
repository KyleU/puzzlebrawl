package models

import java.util.UUID

sealed trait RequestMessage

final case class MalformedRequest(reason: String, content: String) extends RequestMessage

final case class Ping(timestamp: Long) extends RequestMessage
case object GetVersion extends RequestMessage
final case class SetPreference(name: String, value: String) extends RequestMessage

final case class FeedbackResponse(contact: String, feedback: String) extends RequestMessage
final case class DebugInfo(data: String) extends RequestMessage

final case class StartBrawl(scenario: String) extends RequestMessage
final case class JoinBrawl(id: UUID) extends RequestMessage
final case class ObserveBrawl(id: UUID, as: Option[UUID]) extends RequestMessage

sealed trait BrawlMessage extends RequestMessage

final case class SelectTarget(target: UUID) extends BrawlMessage
final case class ResignBrawl(id: UUID) extends BrawlMessage

sealed trait ActiveGemsMessage extends BrawlMessage

case object ActiveGemsLeft extends ActiveGemsMessage
case object ActiveGemsRight extends ActiveGemsMessage
case object ActiveGemsClockwise extends ActiveGemsMessage
case object ActiveGemsCounterClockwise extends ActiveGemsMessage
case object ActiveGemsStep extends ActiveGemsMessage
case object ActiveGemsDrop extends ActiveGemsMessage
