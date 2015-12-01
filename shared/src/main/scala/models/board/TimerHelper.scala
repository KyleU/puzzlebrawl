package models.board

import models.board.mutation.Mutation.ChangeGem
import models.board.mutation.UpdateSegment

trait TimerHelper { this: Board =>
  def decrementTimers() = {
    val ret = mapGems { (gem, x, y) =>
      gem.timer match {
        case Some(v) => Seq(applyMutation(ChangeGem(gem.copy(timer = if (v == 1) { None } else { Some(v - 1) }), x, y)))
        case None => Seq.empty
      }
    }.flatten
    if (ret.isEmpty) { None } else { Some(UpdateSegment("timer", ret)) }
  }
}
