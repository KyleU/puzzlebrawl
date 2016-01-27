package services.brawl

import java.util.UUID

import akka.actor.ActorRef
import models._
import models.user.PlayerRecord
import play.api.libs.concurrent.Akka
import utils.DateUtils

trait ConnectionHelper { this: BrawlService =>
  protected[this] def handleAddPlayer(userId: UUID, name: String, connectionId: UUID, connectionActor: ActorRef) {
    players.find(_.userId == userId) match {
      case Some(player) =>
        player.connectionActor.foreach(_ ! Disconnected("Joined from another connection."))
        player.connectionId = Some(connectionId)
        player.connectionActor = Some(connectionActor)

        connectionActor ! BrawlStarted(brawl.id, self, DateUtils.fromMillis(brawl.started))
        connectionActor ! BrawlJoined(player.userId, brawl)
      case None =>
        // playerConnections += PlayerRecord(userId, name, Some(connectionId), Some(connectionActor))
        //brawl.addPlayer(userId, name)
        throw new NotImplementedError("AddPlayer")
    }
  }

  protected[this] def handleAddObserver(userId: UUID, name: String, connectionId: UUID, connectionActor: ActorRef, as: Option[UUID]) {
    observerConnections += (PlayerRecord(userId, name, Some(connectionId), Some(connectionActor)) -> as)
    connectionActor ! BrawlJoined(userId, brawl)
  }

  protected[this] def handleConnectionStopped(connectionId: UUID) {
    players.find(_.connectionId.contains(connectionId)) match {
      case Some(player) => removePlayer(player.userId)
      case None => observerConnections.find(_._1.connectionId.contains(connectionId)) match {
        case Some(observerConnection) => removeObserver(observerConnection._1.userId)
        case None => log.warn(s"Unknown connection [$connectionId].")
      }
    }
  }

  private[this] def stopIfEmpty() = {
    import play.api.Play.current
    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    import scala.concurrent.duration._

    val hasPlayer = players.exists(_.connectionId.isDefined) || observerConnections.exists(_._1.connectionId.isDefined)
    if (!hasPlayer) {
      Akka.system.scheduler.scheduleOnce(30.seconds, self, StopBrawlIfEmpty)
    }
  }

  protected[this] def removePlayer(userId: UUID) = {
    val player = players.find(_.userId == userId).getOrElse(throw new IllegalStateException(s"Unknown player [$userId]."))
    player.connectionId = None
    player.connectionActor = None
    log.info(s"Player [$userId] removed from brawl [$id].")
    stopIfEmpty()
  }

  protected[this] def removeObserver(userId: UUID) = {
    val observer = observerConnections.find(_._1.userId == userId).getOrElse(throw new IllegalStateException(s"Unknown observer [$userId]."))
    observer._1.connectionId = None
    observer._1.connectionActor = None
    log.info(s"Observer [$userId] removed from brawl [$id].")
    stopIfEmpty()
  }
}
