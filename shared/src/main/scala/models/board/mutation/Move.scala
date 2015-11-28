package models.board.mutation

import models.board.Board
import models.board.mutation.Mutation.MoveGem

object Move {
  def apply(b: Board, m: MoveGem) = {
    val gem = b.at(m.x, m.y).getOrElse(throw new IllegalStateException(s"Move attempted from empty position [${m.x}, ${m.y}]."))
    val (startX, startY) = b.startIndexFor(gem, m.x, m.y)
    for (y <- 0 until gem.height.getOrElse(1)) {
      for (x <- 0 until gem.width.getOrElse(1)) {
        val src = (startX + x, startY + y)
        val tgt = (x, y)
        b.set(src._1, src._2, None)
        b.set(src._1 + m.xDelta, src._2 + m.yDelta, Some(gem))
      }
    }
    MoveGem(startX, startY, m.xDelta, m.yDelta)
  }
}
