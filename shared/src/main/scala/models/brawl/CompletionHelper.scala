package models.brawl

import java.util.{ Date, UUID }

import models.BrawlCompletionReport

trait CompletionHelper { this: Brawl =>
  def onLoss(playerId: UUID) = {
    val p = playersById(playerId)
    p.status = "loss"
    p.completed = Some(new Date().getTime)
    callbacks.foreach(_.onLoss(playerId))
    val teams = players.groupBy(_.team).map(x => x._1 -> x._2.exists(_.status == "active"))
    if (teams.count(_._2) < 2) {
      onComplete()
    }
  }

  private[this] def onComplete() = {
    status = "complete"
    players.filter(_.status == "active").foreach { p =>
      p.status = "win"
      p.completed = Some(new Date().getTime)
    }
    callbacks.foreach(_.onComplete())
  }

  def getCompletionReport = BrawlCompletionReport(
    id = id,
    scenario = scenario,
    durationMs = elapsedMs,
    results = players.map { p =>
      PlayerResult(
        id = p.id,
        name = p.name,
        script = p.script,
        team = p.team,
        score = p.score,
        normalGemCount = p.board.getNormalGemCount,
        timerGemCount = p.board.getTimerGemCount,
        moveCount = p.board.getMoveCount,
        status = p.status,
        completed = p.completed
      )
    }
  )
}
