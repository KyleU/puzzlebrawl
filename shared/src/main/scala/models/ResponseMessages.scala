package models

import java.util.UUID

import models.board.mutation.{ UpdateSegment, Mutation }
import models.brawl.{ PlayerResult, Brawl }

sealed trait ResponseMessage

case class ServerError(reason: String, content: String) extends ResponseMessage
case class Pong(timestamp: Long) extends ResponseMessage
case class VersionResponse(version: String) extends ResponseMessage
case object SendTrace extends ResponseMessage
case class DebugResponse(key: String, data: String) extends ResponseMessage
case class Disconnected(reason: String) extends ResponseMessage

case class BrawlJoined(self: UUID, brawl: Brawl) extends ResponseMessage

case class PlayerUpdate(id: UUID, segments: Seq[UpdateSegment]) extends ResponseMessage
case class PlayerLoss(id: UUID) extends ResponseMessage
case class BrawlCompletionReport(id: UUID, scenario: String, durationMs: Int, results: Seq[PlayerResult]) extends ResponseMessage

case class MessageSet(messages: Seq[ResponseMessage]) extends ResponseMessage
