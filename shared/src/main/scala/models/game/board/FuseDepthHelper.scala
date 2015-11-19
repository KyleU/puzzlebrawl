package models.game.board

import models.game.gem.Gem

object FuseDepthHelper {
  private[this] def canFuse(src: Gem, tgt: Gem) = (!tgt.crash) && tgt.timer.isEmpty && src.color == tgt.color

  def fuseUpDepth(b: Board, gem: Gem, xStart: Int, yStart: Int, width: Int): Int = {
    val gems = (xStart to (xStart + width - 1)).flatMap(xVal => b.at(xVal, yStart).map(g => (g, xVal, yStart)))
    if (gems.size != width) {
      0 // Missing Gems
    } else if (gems.exists(g => g._1.width.isDefined)) {
      val widthGems = gems.filter(_._1.width.isDefined).distinct
      val outOfBounds = widthGems.exists { g =>
        false
      }
      if(outOfBounds) {
        0
      } else {
        val maxHeight = gems.map(_._1.height.getOrElse(1)).max
        maxHeight
      }
    } else {
      if (gems.exists(g => !canFuse(gem, g._1))) {
        0 // Can't Fuse
      } else {
        1 // Expand by 1
      }
    }
  }

  def fuseRightDepth(b: Board, gem: Gem, xStart: Int, yStart: Int, height: Int) = {
    val gems = (yStart to (yStart + height - 1)).flatMap(yVal => b.at(xStart, yVal).map(g => (g, xStart, yVal)))
    if (gems.size != height) {
      0 // Missing Gems
    } else if (gems.exists(g => g._1.height.isDefined)) {
      val heightGems = gems.filter(_._1.height.isDefined).distinct
      val outOfBounds = heightGems.exists { g =>
        false
      }
      if(outOfBounds) {
        0
      } else {
        val maxWidth = heightGems.map(_._1.width.getOrElse(1)).max
        maxWidth
      }
    } else {
      if (gems.exists(g => !canFuse(gem, g._1))) {
        0 // Can't Fuse
      } else {
        1 // Expand by 1
      }
    }
  }

}
