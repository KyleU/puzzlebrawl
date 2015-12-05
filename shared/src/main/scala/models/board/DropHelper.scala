package models.board

import models.board.mutation.Mutation.MoveGem

trait DropHelper { this: Board =>
  def drop(x: Int, y: Int = height) = {
    val gem = at(x, y).getOrElse(throw new IllegalStateException("No gem at [" + x + ", " + y + "] to drop."))
    val yOpt = (0 until height).reverseIterator.drop(height - y).find(i => at(x, i).isDefined) match {
      case Some(yMatch) if yMatch == height - 1 => None
      case Some(yMatch) => Some(yMatch + 1)
      case None => Some(0)
    }
    yOpt match {
      case Some(yIdx) => Seq(applyMutation(MoveGem(x, y, 0, yIdx - y)))
      case None => throw new IllegalStateException(s"Attempt to drop $gem at column [$x], which is full.")
    }
  }
}
