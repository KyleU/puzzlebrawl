package services.brawl

import akka.actor.PoisonPill
import models._
import utils.DateUtils

import scala.util.control.NonFatal

trait MessageHelper { this: BrawlService =>
  protected[this] def handleStopBrawlIfEmpty() {
    val hasPlayer = players.exists(_.connectionId.isDefined) || observerConnections.exists(_._1.connectionId.isDefined)
    if (!hasPlayer) { self ! StopBrawl }
  }

  protected[this] def handleStopBrawl() {
    log.info(s"Stopping brawl [${brawl.id}].")
    context.parent ! BrawlStopped(brawl.id)
    self ! PoisonPill
  }

  protected[this] def sendToAll(messages: Seq[ResponseMessage]): Unit = {
    if (messages.isEmpty) {
      log.info(s"No messages to send to all players for game [$scenario:$seed] in context [$context].")
    } else if (messages.tail.isEmpty) {
      sendToAll(messages.headOption.getOrElse(throw new IllegalStateException()))
    } else {
      sendToAll(MessageSet(messages))
    }
  }

  protected[this] def sendToAll(message: ResponseMessage): Unit = {
    players.foreach(_.connectionActor.foreach(_ ! message))
    observerConnections.foreach(_._1.connectionActor.foreach(_ ! message))
  }

  protected[this] def handleBrawlRequest(br: BrawlRequest) = {
    log.debug("Handling [" + br.message.getClass.getSimpleName.stripSuffix("$") + "] message from user [" + br.userId + "] for brawl [" + brawl.id + "].")
    try {
      val time = DateUtils.now
      brawlMessages += ((br.message, br.userId, time))
      if (firstMoveMade.isEmpty) {
        firstMoveMade = Some(time)
      }
      lastMoveMade = Some(time)
      val player = brawl.playersById(br.userId)
      br.message match {
        case x if brawl.completed.isDefined => log.warn(s"Received brawl message [${x.getClass.getSimpleName}] for completed brawl [$brawl.id].")
        case ActiveGemsLeft => player.activeGemsLeft().foreach { m =>
          player.board.moveCount += 1
          sendToAll(PlayerUpdate.using(player.id, "active", m))
        }
        case ActiveGemsRight => player.activeGemsRight().foreach { m =>
          player.board.moveCount += 1
          sendToAll(PlayerUpdate.using(player.id, "active", m))
        }
        case ActiveGemsClockwise => player.activeGemsClockwise().foreach { ms =>
          player.board.moveCount += 1
          sendToAll(PlayerUpdate.using(player.id, "active", ms))
        }
        case ActiveGemsCounterClockwise => player.activeGemsCounterClockwise().foreach { ms =>
          player.board.moveCount += 1
          sendToAll(PlayerUpdate.using(player.id, "active", ms))
        }
        case ActiveGemsStep => player.activeGemsStep().foreach { m =>
          player.board.moveCount += 1
          sendToAll(PlayerUpdate.using(player.id, "active", m))
        }
        case ActiveGemsDrop =>
          player.board.moveCount += 1
          sendToAll(PlayerUpdate(player.id, player.activeGemsDrop() +: player.board.fullTurn() :+ player.activeGemsCreate()))
        case r => log.warn(s"GameService received unknown brawl message [${r.getClass.getSimpleName.stripSuffix("$")}].")
      }
    } catch {
      case NonFatal(x) =>
        log.error(s"Exception processing brawl request [$br].", x)
        sender() ! ServerError(x.getClass.getSimpleName, x.getMessage)
    }
  }

  protected[this] def handleInternalMessage(im: InternalMessage) = {
    log.debug("Handling [" + im.getClass.getSimpleName.stripSuffix("$") + "] internal message for game [" + id + "].")
    try {
      im match {
        case ap: AddPlayer => timeReceive(ap) { handleAddPlayer(ap.userId, ap.name, ap.connectionId, ap.connectionActor) }
        case ao: AddObserver => timeReceive(ao) { handleAddObserver(ao.userId, ao.name, ao.connectionId, ao.connectionActor, ao.as) }
        case cs: ConnectionStopped => timeReceive(cs) { handleConnectionStopped(cs.connectionId) }
        case StopBrawl => timeReceive(StopBrawl) { handleStopBrawl() }
        case StopBrawlIfEmpty => timeReceive(StopBrawlIfEmpty) { handleStopBrawlIfEmpty() }
        case gt: BrawlTrace => timeReceive(gt) { handleBrawlTrace() }
        case BrawlUpdate => timeReceive(BrawlUpdate) { handleBrawlUpdate() }
        case _ => log.warn(s"GameService received unhandled internal message [${im.getClass.getSimpleName}].")
      }
    } catch {
      case NonFatal(x) => log.error(s"Exception processing internal message [$im].", x)
    }
  }
}
