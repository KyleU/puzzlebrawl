package models

import java.util.UUID

import models.board.mutation.{ UpdateSegment, Mutation }
import models.brawl.Brawl

sealed trait ResponseMessage
trait ReversibleResponseMessage extends ResponseMessage

case class ServerError(reason: String, content: String) extends ResponseMessage
case class Pong(timestamp: Long) extends ResponseMessage
case class VersionResponse(version: String) extends ResponseMessage
case object SendTrace extends ResponseMessage
case class DebugResponse(key: String, data: String) extends ResponseMessage
case class Disconnected(reason: String) extends ResponseMessage

case class BrawlJoined(self: UUID, brawl: Brawl, elapsedMs: Int) extends ResponseMessage

object PlayerUpdate {
  def using(id: UUID, category: String, m: Mutation) = PlayerUpdate(id, Seq(UpdateSegment(category, Seq(m))))
  def using(id: UUID, category: String, ms: Seq[Mutation]) = PlayerUpdate(id, Seq(UpdateSegment(category, ms)))
}
case class PlayerUpdate(id: UUID, segments: Seq[UpdateSegment]) extends ResponseMessage

case class MessageSet(messages: Seq[ResponseMessage]) extends ReversibleResponseMessage
