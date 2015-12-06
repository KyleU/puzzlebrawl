package models.board.mutation

import models.board.Board
import models.board.mutation.Mutation.{ MoveGems, MoveGem }

object Move {
  def apply(b: Board, m: MoveGem) = {
    val gem = b.at(m.x, m.y).getOrElse(throw new IllegalStateException(s"Move attempted from empty position [${m.x}, ${m.y}]."))
    val (startX, startY) = b.startIndexFor(gem, m.x, m.y)
    for (y <- 0 until gem.height.getOrElse(1)) {
      for (x <- 0 until gem.width.getOrElse(1)) {
        val src = (startX + x, startY + y)
        b.set(src._1, src._2, None)
        b.set(src._1 + m.xDelta, src._2 + m.yDelta, Some(gem))
      }
    }
    MoveGem(startX, startY, m.xDelta, m.yDelta)
  }

  def apply(b: Board, ms: MoveGems) = {
    val gems = ms.moves.map { m =>
      val gem = b.at(m.x, m.y).getOrElse(throw new IllegalStateException(s"Move attempted from empty position [${m.x}, ${m.y}]."))
      val start = b.startIndexFor(gem, m.x, m.y)
      for (y <- 0 until gem.height.getOrElse(1)) {
        for (x <- 0 until gem.width.getOrElse(1)) {
          val src = (start._1 + x, start._2 + y)
          b.set(src._1, src._2, None)
        }
      }
      gem -> start
    }
    ms.moves.zipWithIndex.map { case (m, i) =>
      val gem = gems(i)
      for (y <- 0 until gem._1.height.getOrElse(1)) {
        for (x <- 0 until gem._1.width.getOrElse(1)) {
          val src = (gem._2._1 + x, gem._2._2 + y)
          b.set(src._1 + m.xDelta, src._2 + m.yDelta, Some(gem._1))
        }
      }
      MoveGem(gem._2._1, gem._2._2, m.xDelta, m.yDelta)
    }
    ms
  }
}
