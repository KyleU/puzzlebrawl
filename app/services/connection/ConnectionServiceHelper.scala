package services.connection

import java.util.UUID

import akka.actor.ActorRef
import models._
import org.joda.time.LocalDateTime
import utils.Logging
import utils.metrics.InstrumentedActor

trait ConnectionServiceHelper
    extends InstrumentedActor
    with ConnectionServicePreferenceHelper
    with ConnectionServiceTraceHelper
    with Logging { this: ConnectionService =>

  protected[this] def handleStartBrawl(scenario: String, seed: Option[Int]) = {
    supervisor ! CreateBrawl(scenario, id, seed)
  }

  protected[this] def handleJoinBrawl(brawlId: UUID) = {
    supervisor ! ConnectionBrawlJoin(brawlId, id)
  }

  protected[this] def handleObserveBrawl(brawlId: UUID, as: Option[UUID]) = {
    supervisor ! ConnectionBrawlObserve(brawlId, id, as)
  }

  protected[this] def handleBrawlMessage(bm: BrawlMessage) = activeBrawl match {
    case Some(ag) => ag forward BrawlRequest(user.id, bm)
    case None => throw new IllegalArgumentException(s"Received brawl message [${bm.getClass.getSimpleName}] while not in brawl.")
  }

  protected[this] def handleBrawlStarted(id: UUID, brawlService: ActorRef, started: LocalDateTime) {
    activeBrawlId = Some(id)
    activeBrawl = Some(brawlService)
  }

  protected[this] def handleBrawlStopped(id: UUID) {
    if (!activeBrawlId.contains(id)) {
      //throw new IllegalStateException(s"Provided brawl [$id] is not the active brawl.")
    }
    activeBrawlId = None
    activeBrawl = None
  }

  protected[this] def handleResponseMessage(rm: ResponseMessage) {
    out ! rm
  }
}
