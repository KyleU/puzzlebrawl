import models.BrawlCompletionReport
import models.brawl.PlayerResult

trait CheatHelper { this: PuzzleBrawl =>
  protected[this] def handleCheat(key: String) = key match {
    case "victory" => send(getCompletionReport)
    case _ => throw new IllegalStateException(s"Unknown cheat [$key].")
  }

  private[this] def getCompletionReport = {
    val brawl = activeBrawl.getOrElse(throw new IllegalStateException())
    BrawlCompletionReport(
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
          gemCount = p.board.getGemCount,
          moveCount = p.board.getMoveCount
        )
      }
    )
  }
}
