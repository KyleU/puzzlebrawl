package services.brawl

import java.util.UUID

import models.{ ActiveGemsStep, BrawlMessage }
import org.joda.time.LocalDateTime
import services.console.TextGemPattern

object DebugHelper {
  val recordMessages = false
  val logMessages = false
  val logBoardState = false
}

trait DebugHelper { this: BrawlService =>
  protected[this] var playerMessageCounts = collection.mutable.HashMap(players.map(_.userId -> 0): _*)
  protected[this] val lastBrawlMessages = collection.mutable.HashMap[UUID, Option[(BrawlMessage, LocalDateTime)]](players.map(_.userId -> None): _*)
  protected[this] val brawlMessages = if (DebugHelper.recordMessages) {
    Some(collection.mutable.ArrayBuffer.empty[(BrawlMessage, UUID, LocalDateTime)])
  } else {
    None
  }

  protected[this] def logBoards() = brawl.players.foreach { p =>
    val board = TextGemPattern.crudeRenderingOf(p.board)
    log.info(s"Board for player [${p.id}]:\n  " + board.mkString("\n  "))
  }

  protected[this] def logBrawlMessageReceive(message: BrawlMessage, playerId: UUID, occurred: LocalDateTime) = message match {
    case ActiveGemsStep => // No op.
    case _ =>
      playerMessageCounts(playerId) += 1
      lastBrawlMessages(playerId) = Some(message -> occurred)
      brawlMessages.map(_ += ((message, playerId, occurred)))
      if (DebugHelper.logMessages) {
        log.info(s"Message [${utils.Formatter.className(message)}] received for player [$playerId].")
      }
      if (DebugHelper.logBoardState) {
        val before = TextGemPattern.crudeRenderingOf(brawl.playersById(playerId).board)
        log.info(s"Board for player [$playerId] before message:\n  " + before.mkString("\n  "))
      }
  }

  protected[this] def logBrawlMessageComplete(message: BrawlMessage, playerId: UUID, occurred: LocalDateTime) = {
    if (DebugHelper.logMessages) {
      val elapsed = utils.DateUtils.nowMillis - utils.DateUtils.toMillis(occurred)
      log.info(s"Message [${utils.Formatter.className(message)}] completed for player [$playerId] in [${elapsed}ms].")
    }
    if (DebugHelper.logBoardState) {
      val after = TextGemPattern.crudeRenderingOf(brawl.playersById(playerId).board)
      log.info(s"Board for player [$playerId] after message:\n  " + after.mkString("\n  "))
    }
  }
}
