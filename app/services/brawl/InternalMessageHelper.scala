package services.brawl

import akka.actor.PoisonPill
import models._

import scala.util.control.NonFatal

trait InternalMessageHelper { this: BrawlService =>
  private[this] def handleStopBrawlIfEmpty() {
    val hasPlayer = players.exists(_.connectionId.isDefined) || observerConnections.exists(_._1.connectionId.isDefined)
    if (!hasPlayer) { self ! StopBrawl }
  }

  private[this] def handleStopBrawl() {
    log.info(s"Stopping brawl [${brawl.id}].")
    context.parent ! BrawlStopped(brawl.id)
    self ! PoisonPill
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
