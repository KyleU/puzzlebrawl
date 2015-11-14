package models.game.board

import models.game.board.Board.FuseGems
import models.game.gem.Gem

trait FuseHelper { this: Board =>
  def fuse(x: Int, y: Int) = {
    val gem = at(x, y).getOrElse(throw new IllegalStateException("Fuse called for empty space."))
    def canFuse(g: Gem) = (!g.crash) && g.timer.isEmpty && g.color == gem.color

    val eligible = at(x + 1, y).exists(canFuse) && at(x, y + 1).exists(canFuse) && at(x + 1, y + 1).exists(canFuse)
    if(eligible) {
      val msg = FuseGems(groupId = 1, x = x, y = y, width = 2, height = 2)
      applyMutation(msg)
      Seq(msg)
    } else {
      Seq.empty
    }
  }
}
