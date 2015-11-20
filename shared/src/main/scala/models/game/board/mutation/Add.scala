package models.game.board.mutation

import models.game.board.Board
import models.game.board.mutation.Mutation.AddGem

object Add {
  def apply(b: Board, m: AddGem) = {
    val me = Some(m.gem)
    for(y <- 0 until m.gem.height.getOrElse(1)) {
      for(x <- 0 until m.gem.width.getOrElse(1)) {
        b.at(m.x + x, m.y + y) match {
          case Some(g) => throw new IllegalStateException(s"Attempt to add index [$x, $y] of [${m.gem}] to [${m.x}, ${m.y}], which is occupied by [$g].")
          case None => b.set(m.x + x, m.y + y, me)
        }
      }
    }
    m
  }
}
