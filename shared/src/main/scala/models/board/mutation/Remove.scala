package models.board.mutation

import models.board.Board
import models.board.mutation.Mutation.RemoveGem

object Remove {
  def apply(b: Board, m: RemoveGem) = {
    val gem = b.at(m.x, m.y).getOrElse(throw new IllegalStateException(s"Remove attempted from empty position [${m.x}, ${m.y}]."))
    val (startX, startY) = b.startIndexFor(gem, m.x, m.y)

    for (y <- 0 until gem.height.getOrElse(1)) {
      for (x <- 0 until gem.width.getOrElse(1)) {
        b.at(startX + x, startY + y) match {
          case Some(g) => b.set(startX + x, startY + y, None)
          case None => throw new IllegalStateException(s"Remove attempted for [$gem] from empty position [$startX, $startY] at index [$x, $y].")
        }
      }
    }
    RemoveGem(startX, startY)
  }
}
