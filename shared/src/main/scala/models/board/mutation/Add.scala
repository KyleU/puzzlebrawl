package models.board.mutation

import models.board.Board
import models.board.mutation.Mutation.AddGem

object Add {
  def apply(b: Board, m: AddGem) = {
    b.incrementGemCount(m.gem.timer.isDefined)
    for (y <- 0 until m.gem.height.getOrElse(1)) {
      for (x <- 0 until m.gem.width.getOrElse(1)) {
        b.at(m.x + x, m.y + y) match {
          case Some(g) => throw new IllegalStateException(s"Attempt to add index [$x, $y] of [${m.gem}] to [${m.x}, ${m.y}], which is occupied by [$g].")
          case None => b.set(m.x + x, m.y + y, Some(m.gem))
        }
      }
    }
    m
  }
}
