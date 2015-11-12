package models.game.board

import models.game.board.Board.MoveGem

trait CollapseHelper { this: Board =>
  def collapse() = mapGems { (gem, x, y) =>
    val moveIndex = y match {
      case 0 => None
      case _ =>
        val occupied = ((y - 1) to 0 by -1).find { idx =>
          val space = at(x, idx)
          space.isDefined
        }
        occupied match {
          case Some(idx) if idx < y - 1 => Some(idx + 1)
          case Some(idx) => None
          case None => Some(0)
        }
    }
    moveIndex.map { idx =>
      val msg = MoveGem(x, y, x, idx)
      applyMutation(msg)
      msg
    }.toSeq
  }.flatten
}
