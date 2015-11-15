package models.game.board

import models.game.board.Board.FuseGems
import models.game.gem.{ Color, Gem }

trait FuseHelper { this: Board =>
  private[this] def canFuse(gem: Option[Gem], color: Color) = gem.exists(g => g.color == color && (!g.crash) && g.timer.isEmpty && g.group.isEmpty)

  def fuse(x: Int, y: Int) = {
    val gem = at(x, y).getOrElse(throw new IllegalStateException("Fuse called for empty space."))

    val neighbors = Seq(at(x + 1, y), at(x, y + 1), at(x + 1, y + 1)).flatten
    val eligible = neighbors.exists(g => canFuse(Some(g), gem.color))
    if(eligible) {
      val (width, height) = expand(x, y, gem.color, 2, 2)
      val msg = FuseGems(groupId = 1, x = x, y = y, width = width, height = height)
      applyMutation(msg)
      Seq(msg)
    } else {
      Seq.empty
    }
  }

  private[this] def expand(originX: Int, originY: Int, color: Color, width: Int, height: Int): (Int, Int) = {
    println(s"originX: $originX, originY: $originY, color: $color, width: $width, height: $height")
    val matchesTop = if(originY + height > this.height - 1) {
      false
    } else {
      !(0 until width).exists( x => !canFuse(at(originX + x, originY + height), color))
    }
    val topResult = if(matchesTop) {
      expand(originX, originY, color, width, height + 1)
    } else {
      width -> height
    }
    val topArea = topResult._1 * topResult._2

    val matchesRight = if(originX + width > this.width - 1) {
      false
    } else {
      !(0 until height).exists( y => !canFuse(at(originX + width, originY + y), color))
    }
    val rightResult = if(matchesRight) {
      expand(originX, originY, color, width + 1, height)
    } else {
      width -> height
    }
    val rightArea = rightResult._1 * rightResult._2

    if(topArea > rightArea) { topResult } else { rightResult }
  }
}
