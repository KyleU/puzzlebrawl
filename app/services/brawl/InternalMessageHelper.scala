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
    log.debug("Handling [" + utils.Formatter.className(im) + "] internal message for game [" + id + "].")
    try {
      im match {
        case ap: AddPlayer => timeReceive(ap) { handleAddPlayer(ap.userId, ap.name, ap.connectionId, ap.connectionActor) }
        case ao: AddObserver => timeReceive(ao) { handleAddObserver(ao.userId, ao.name, ao.connectionId, ao.connectionActor, ao.as) }
        case cs: ConnectionStopped => timeReceive(cs) { handleConnectionStopped(cs.connectionId) }
        case StopBrawl => timeReceive(StopBrawl) { handleStopBrawl() }
        case StopBrawlIfEmpty => timeReceive(StopBrawlIfEmpty) { handleStopBrawlIfEmpty() }
        case gt: BrawlTrace => timeReceive(gt) { handleBrawlTrace() }
        case s: UpdateSchedule => timeReceive(s) { handleUpdateSchedule(s) }
        case _ => log.warn(s"BrawlService received unhandled internal message [${im.getClass.getSimpleName}].")
      }
    } catch {
      case NonFatal(x) => log.error(s"Exception processing internal message [$im].", x)
    }
  }
}
