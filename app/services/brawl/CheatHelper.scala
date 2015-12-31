package services.brawl

import java.util.UUID

import models.BrawlCompletionReport
import models.brawl.PlayerResult

trait CheatHelper { this: BrawlService =>
  protected[this] def handleCheat(key: String) = key match {
    case "victory" => sender() ! getCompletionReport
    case _ => log.error(s"Unknown cheat [$key].")
  }

  private[this] def getCompletionReport = BrawlCompletionReport(
    id = brawl.id,
    scenario = brawl.scenario,
    durationMs = 0, // TODO
    results = brawl.players.map { p =>
      PlayerResult(
        id = p.id,
        name = p.name,
        script = p.script,
        team = p.team,
        score = p.score,
        normalGemCount = p.board.getNormalGemCount,
        timerGemCount = p.board.getTimerGemCount,
        moveCount = p.board.getMoveCount
      )
    }
  )
}
