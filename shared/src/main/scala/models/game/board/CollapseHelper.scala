package models.game.board

import models.game.board.Board.MoveGem

trait CollapseHelper { this: Board =>
  def collapse() = mapGems { (gem, x, y) =>
    val moveIndex = y match {
      case 0 => None
      case _ => ((y - 1) to 0 by -1).find( idx => at(x, idx).isDefined) match {
        case Some(idx) if idx == y - 1 => None
        case Some(idx) => Some(idx + 1)
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
