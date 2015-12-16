package services.brawl

import models.history.BrawlHistory
import services.history.BrawlHistoryService
import utils.DateUtils

trait HistoryHelper { this: BrawlService =>
  protected[this] var brawlHistory: Option[BrawlHistory] = None

  protected def insertHistory() = {
    brawlHistory = Some(BrawlHistory(
      id = id,
      seed = seed,
      scenario = scenario,
      status = "started",
      players = players.map(_.userId),
      gems = players.map(x => brawl.playersById(x.userId).board.gemCount),
      moves = players.map(x => brawl.playersById(x.userId).board.moveCount),
      started = DateUtils.fromMillis(brawl.started),
      None,
      None,
      None
    ))
    brawlHistory.map(BrawlHistoryService.insert)

  }
}
