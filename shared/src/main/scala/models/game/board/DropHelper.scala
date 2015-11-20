package models.game.board

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.Gem

trait DropHelper { this: Board =>
  def drop(gem: Gem, x: Int) = {
    val col = spaces(x)
    val yOpt = col.indices.reverseIterator.find(i => col(i).isDefined) match {
      case Some(yMatch) if yMatch == height - 1 => None
      case Some(yMatch) => Some(yMatch + 1)
      case None => Some(0)
    }
    yOpt match {
      case Some(y) => Seq(applyMutation(AddGem(gem, x, y)))
      case None => throw new IllegalStateException(s"Attempt to drop $gem at column [$x], which is full.")
    }
  }
}
