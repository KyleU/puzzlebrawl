package models.game.board

import models.game.board.mutation.Mutation.ChangeGem

trait TimerHelper { this: Board =>
  def decrementTimers() = mapGems { (gem, x, y) =>
    gem.timer match {
      case Some(v) => Seq(applyMutation(ChangeGem(gem.copy(timer = if (v == 1) { None } else { Some(v - 1) }), x, y)))
      case None => Seq.empty
    }
  }.flatten
}
