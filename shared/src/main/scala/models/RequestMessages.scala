package models

import java.util.UUID

sealed trait RequestMessage

case class MalformedRequest(reason: String, content: String) extends RequestMessage

case class Ping(timestamp: Long) extends RequestMessage
case object GetVersion extends RequestMessage
case class SetPreference(name: String, value: String) extends RequestMessage

case class DebugRequest(data: String) extends RequestMessage

case class StartBrawl(scenario: String) extends RequestMessage
case class JoinBrawl(id: UUID) extends RequestMessage
case class ObserveBrawl(id: UUID, as: Option[UUID]) extends RequestMessage

trait BrawlMessage extends RequestMessage

case class SelectTarget(t: UUID) extends BrawlMessage

trait ActiveGemsMessage extends BrawlMessage
case object ActiveGemsLeft extends ActiveGemsMessage
case object ActiveGemsRight extends ActiveGemsMessage
case object ActiveGemsClockwise extends ActiveGemsMessage
case object ActiveGemsCounterClockwise extends ActiveGemsMessage
case object ActiveGemsStep extends ActiveGemsMessage
case object ActiveGemsDrop extends ActiveGemsMessage
