package models

import java.util.UUID

import akka.actor.ActorRef
import models.user.User
import org.joda.time.LocalDateTime

sealed trait InternalMessage

case class ConnectionStarted(user: User, connectionId: UUID, conn: ActorRef) extends InternalMessage
case class ConnectionStopped(connectionId: UUID) extends InternalMessage

case class AddPlayer(userId: UUID, name: String, connectionId: UUID, connectionActor: ActorRef) extends InternalMessage
case class AddObserver(userId: UUID, name: String, connectionId: UUID, connectionActor: ActorRef, as: Option[UUID]) extends InternalMessage

case class CreateBrawl(scenario: String, connectionId: UUID, seed: Option[Int]) extends InternalMessage
case class BrawlStarted(id: UUID, brawlService: ActorRef, started: LocalDateTime) extends InternalMessage
case class ConnectionBrawlJoin(id: UUID, connectionId: UUID) extends InternalMessage
case class ConnectionBrawlObserve(id: UUID, connectionId: UUID, as: Option[UUID]) extends InternalMessage
case class BrawlStopped(id: UUID) extends InternalMessage

case object StopBrawl extends InternalMessage
case object StopBrawlIfEmpty extends InternalMessage

case class BrawlRequest(userId: UUID, message: BrawlMessage) extends InternalMessage

case class UpdateSchedule(id: UUID, script: String, minActionMs: Int, maxActionMs: Int) extends InternalMessage

case object GetSystemStatus extends InternalMessage
case class SystemStatus(brawls: Seq[(UUID, Seq[(UUID, String)])], connections: Seq[(UUID, String)]) extends InternalMessage

case class ConnectionTrace(id: UUID) extends InternalMessage
case class ClientTrace(id: UUID) extends InternalMessage
case class BrawlTrace(id: UUID) extends InternalMessage
case class TraceResponse(id: UUID, data: Seq[(String, Any)]) extends InternalMessage

