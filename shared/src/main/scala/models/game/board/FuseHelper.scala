package models.game.board

import models.game.board.Board.{ ChangeGem, RemoveGem }
import models.game.gem.Gem

trait FuseHelper { this: Board =>
  def fuse() = mapGems { (gem, x, y) =>
    val (width, height) = if(gem.width.getOrElse(1) == 1 && gem.height.getOrElse(1) == 1) {
      if(canFuse(gem, x + 1, y) && canFuse(gem, x, y + 1) && canFuse(gem, x + 1, y + 1)) {
        largestSize(gem, x, y, 2, 2)
      } else {
        1 -> 1
      }
    } else {
      largestSize(gem, x, y, gem.width.getOrElse(1), gem.height.getOrElse(1))
    }

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

  private[this] def largestSize(gem: Gem, x: Int, y: Int, width: Int, height: Int) = {
    val checkedDimensions = collection.mutable.HashSet.empty[(Int, Int)]

    def expand(gem: Gem, x: Int, y: Int, width: Int, height: Int): (Int, Int) = {
      println(s"width: $width, height: $height")
      checkedDimensions += (width -> height)

      val expandsUp = !(0 until width).exists { xOffset =>
        !canFuse(gem, x + xOffset, y + height)
      }
      val upResult = if (expandsUp) {
        if (checkedDimensions(width -> (height + 1))) {
          width -> (height + 1)
        } else {
          expand(gem, x, y, width, height + 1)
        }
      } else {
        width -> height
      }

      val expandsRight = !(0 until height).exists { yOffset =>
        !canFuse(gem, x + width, y + yOffset)
      }
      val rightResult = if (expandsRight) {
        if (checkedDimensions((width + 1) -> height)) {
          (width + 1) -> height
        } else {
          expand(gem, x, y, width + 1, height)
        }
      } else {
        width -> height
      }

      if ((upResult._1 * upResult._2) > (rightResult._1 * rightResult._2)) {
        upResult
      } else {
        rightResult
      }
    }

    expand(gem, x, y, gem.width.getOrElse(1), gem.height.getOrElse(1))
  }

  private[this] def canFuse(gem: Gem, x: Int, y: Int) = at(x, y) match {
    case Some(g) => (!g.crash) && g.timer.isEmpty && g.color == gem.color
    case None => false
  }
}
