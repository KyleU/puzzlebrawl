package services.brawl

import java.util.UUID

import models.board.mutation.Mutation.TargetChanged
import models.board.mutation.UpdateSegment
import models.brawl.Brawl
import models._
import play.api.libs.concurrent.Akka
import utils.Logging
import utils.metrics.InstrumentedActor

import scala.util.Random

trait BrawlHelper
    extends InstrumentedActor
    with Brawl.Callbacks
    with BrawlMessageHelper
    with ConnectionHelper
    with DebugHelper
    with HistoryHelper
    with InternalMessageHelper
    with Logging
    with RequestHelper
    with TraceHelper
    with UpdateHelper { this: BrawlService =>

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

  protected[this] def onResign(brawlId: UUID, playerId: UUID) = {
    if (brawlId != id) {
      throw new IllegalStateException(s"Tried to resign brawl [$brawlId], but active brawl is [$id].")
    }
    log.info(s"Player [$playerId] has resigned from brawl [$id].")
    brawl.onLoss(playerId)
  }

  override def onLoss(playerId: UUID) = {
    sendToAll(PlayerLoss(playerId))
    brawl.players.filter(_.target.contains(playerId)).foreach { p =>
      val validPlayers = Random.shuffle(brawl.players.filter(player => player.isActive && player.id != p.id).map(_.id))
      p.target = validPlayers.headOption
      p.target.foreach { tgt =>
        sendToAll(PlayerUpdate(p.id, Seq(UpdateSegment("target", Seq(TargetChanged(tgt))))))
      }
    }
  }

  override def onComplete() = {
    val report = brawl.getCompletionReport
    sendToAll(report)
    val duration = utils.Formatter.withCommas(report.durationMs)
    val totalMoves = report.results.map(_.moveCount).sum
    log.info(s"Brawl [$id] completed after [${duration}ms] and [$totalMoves] total moves.")
    players.foreach(p => removePlayer(p.userId))
  }
}
