package models

import java.util.UUID

sealed trait RequestMessage

case class MalformedRequest(reason: String, content: String) extends RequestMessage

case class Ping(timestamp: Long) extends RequestMessage
case object GetVersion extends RequestMessage
case class SetPreference(name: String, value: String) extends RequestMessage

case class DebugInfo(data: String) extends RequestMessage

case class StartBrawl(scenario: String) extends RequestMessage
case class JoinBrawl(id: UUID) extends RequestMessage
case class ObserveBrawl(id: UUID, as: Option[UUID]) extends RequestMessage

trait BrawlMessage extends RequestMessage

case object ActiveGemsLeft extends BrawlMessage
case object ActiveGemsRight extends BrawlMessage
case object ActiveGemsClockwise extends BrawlMessage
case object ActiveGemsCounterClockwise extends BrawlMessage
case object ActiveGemsStep extends BrawlMessage
case object ActiveGemsDrop extends BrawlMessage
