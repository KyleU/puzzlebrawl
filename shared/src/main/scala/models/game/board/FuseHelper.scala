package models.game.board

import models.game.board.Board.{ ChangeGem, RemoveGem }
import models.game.gem.Gem

trait FuseHelper { this: Board =>
  def fuse() = mapGems { (gem, x, y) =>
    val (width, height) = expand(gem, x, y)
    if(width > gem.width.getOrElse(1) || height > gem.height.getOrElse(1)) {
      val removals = (0 until height).flatMap { yOffset =>
        (0 until width).flatMap { xOffset =>
          val testGem = at(x + xOffset, y + yOffset).getOrElse(s"Unable to fuse missing gem at [${x + xOffset}, ${y + yOffset}].")
          if(testGem == gem) {
            None
          } else {
            val msg = RemoveGem(x + xOffset, y + yOffset)
            applyMutation(msg)
            Some(msg)
          }
        }
      }
      val changeMsg = ChangeGem(gem.copy(width = Some(width), height = Some(height)), x, y)
      applyMutation(changeMsg)
      removals :+ changeMsg
    } else {
      Seq.empty
    }
  }.flatten

  private[this] def expand(gem: Gem, x: Int, y: Int): (Int, Int) = {
    expand(gem, x, y, gem.width.getOrElse(1), gem.height.getOrElse(1))
  }

  private[this] def expand(gem: Gem, x: Int, y: Int, width: Int, height: Int): (Int, Int) = {
    if(x == 0 && y == 0) {
      (3, 3)
    } else {
      (width, height)
    }
  }
}
