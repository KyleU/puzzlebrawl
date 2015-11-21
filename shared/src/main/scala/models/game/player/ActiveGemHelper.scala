package models.game.player

import models.game.gem.GemLocation

trait ActiveGemHelper { this: Player =>
  def createActiveGems() = {
    if (activeGems.nonEmpty) {
      throw new IllegalStateException(s"Active gems created, but player [$id] already has active gems [${activeGems.mkString(", ")}].")
    }

    val x = (board.width / 2) - 1
    val y = board.height - 1
    activeGems = Seq(GemLocation(gemStream.next, x, y), GemLocation(gemStream.next, x + 1, y))
  }
}
