package models

import java.util.UUID

import models.board.mutation.UpdateSegment
import models.brawl.{ PlayerResult, Brawl }
import models.user.UserPreferences

sealed trait ResponseMessage

final case class ServerError(reason: String, content: String) extends ResponseMessage
final case class VersionResponse(version: String) extends ResponseMessage

final case class InitialState(userId: UUID, username: Option[String], preferences: UserPreferences) extends ResponseMessage

final case class Pong(timestamp: Long) extends ResponseMessage
case object SendTrace extends ResponseMessage
final case class DebugResponse(key: String, data: String) extends ResponseMessage
final case class Disconnected(reason: String) extends ResponseMessage

final case class BrawlQueueUpdate(scenario: String, requiredPlayers: Int, players: Seq[String]) extends ResponseMessage
final case class BrawlJoined(self: UUID, brawl: Brawl) extends ResponseMessage

final case class PlayerUpdate(id: UUID, segments: Seq[UpdateSegment]) extends ResponseMessage
final case class PlayerLoss(id: UUID) extends ResponseMessage
final case class BrawlCompletionReport(id: UUID, scenario: String, durationMs: Int, results: Seq[PlayerResult]) extends ResponseMessage

final case class MessageSet(messages: Seq[ResponseMessage]) extends ResponseMessage
