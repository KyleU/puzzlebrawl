package models

import java.util.UUID

import akka.actor.ActorRef
import models.user.User
import org.joda.time.LocalDateTime
import play.api.libs.json.JsObject

sealed trait InternalMessage

final case class ConnectionStarted(user: User, connectionId: UUID, conn: ActorRef) extends InternalMessage
final case class ConnectionStopped(connectionId: UUID) extends InternalMessage

final case class AddPlayer(userId: UUID, name: String, connectionId: UUID, connectionActor: ActorRef) extends InternalMessage
final case class AddObserver(userId: UUID, name: String, connectionId: UUID, connectionActor: ActorRef, as: Option[UUID]) extends InternalMessage

final case class CreateBrawl(scenario: String, connectionId: UUID, seed: Option[Int]) extends InternalMessage
final case class BrawlStarted(id: UUID, brawlService: ActorRef, started: LocalDateTime) extends InternalMessage
final case class ConnectionBrawlJoin(id: UUID, connectionId: UUID) extends InternalMessage
final case class ConnectionBrawlObserve(id: UUID, connectionId: UUID, as: Option[UUID]) extends InternalMessage
final case class BrawlStopped(id: UUID) extends InternalMessage

case object StopBrawl extends InternalMessage
case object StopBrawlIfEmpty extends InternalMessage

final case class BrawlRequest(userId: UUID, message: BrawlMessage) extends InternalMessage

final case class UpdateSchedule(id: UUID, script: String, minActionMs: Int, maxActionMs: Int) extends InternalMessage

case object GetSystemStatus extends InternalMessage
final case class SystemStatus(brawls: Seq[(UUID, Seq[(UUID, String)])], connections: Seq[(UUID, String)]) extends InternalMessage

final case class SendConnectionTrace(id: UUID) extends InternalMessage
final case class ConnectionTraceResponse(id: UUID, userId: UUID, username: Option[String]) extends InternalMessage

final case class SendClientTrace(id: UUID) extends InternalMessage
final case class ClientTraceResponse(id: UUID, data: JsObject) extends InternalMessage

final case class SendBrawlTrace(id: UUID) extends InternalMessage
final case class BrawlTraceResponse(
  id: UUID, scenario: String, seed: Int, started: Long,
  players: Seq[(UUID, String, Option[UUID], Int, Option[(BrawlMessage, LocalDateTime)])],
  observers: Seq[(UUID, String, Option[UUID])]) extends InternalMessage
