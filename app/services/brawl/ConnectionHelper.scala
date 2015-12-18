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
        connectionActor ! BrawlJoined(player.userId, brawl, elapsedSeconds.getOrElse(0))
      case None =>
        // playerConnections += PlayerRecord(userId, name, Some(connectionId), Some(connectionActor))
        //brawl.addPlayer(userId, name)
        throw new NotImplementedError("AddPlayer")
    }
  }

  protected[this] def handleAddObserver(userId: UUID, name: String, connectionId: UUID, connectionActor: ActorRef, as: Option[UUID]) {
    observerConnections += (PlayerRecord(userId, name, Some(connectionId), Some(connectionActor)) -> as)
    connectionActor ! BrawlJoined(userId, brawl, elapsedSeconds.getOrElse(0))
  }

  protected[this] def handleConnectionStopped(connectionId: UUID) {
    import play.api.Play.current
    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    import scala.concurrent.duration._

    val player = players.find(_.connectionId.contains(connectionId)).getOrElse(throw new IllegalArgumentException(s"Unknown connection [$connectionId]."))
    if (player.connectionId.contains(connectionId)) {
      log.info(s"Player connection [$connectionId] stopped.")
      player.connectionId = None
      player.connectionActor = None
    }
    observerConnections.find(_._1.connectionId.contains(connectionId)) match {
      case Some(observerConnection) =>
        log.info(s"Observer connection [$connectionId] stopped.")
        observerConnection._1.connectionId = None
        observerConnection._1.connectionActor = None
      case None => // noop
    }

    val hasPlayer = players.exists(_.connectionId.isDefined) || observerConnections.exists(_._1.connectionId.isDefined)
    if (!hasPlayer) {
      Akka.system.scheduler.scheduleOnce(30.seconds, self, StopBrawlIfEmpty)
    }
  }
}
