package models.game.board

import models.game.gem.Gem

import scala.annotation.tailrec

object FuseDepthHelper {
  private[this] def canFuse(src: Gem, tgt: Gem) = (!tgt.crash) && tgt.timer.isEmpty && src.color == tgt.color

  @tailrec
  def fuseUpDepth(b: Board, gem: Gem, xStart: Int, yStart: Int, width: Int, pendingCols: Int): Int = {
    val gems = (0 until width).flatMap(xVal => b.at(xStart + xVal, yStart + 1))
    if(gems.size != width) {
      0 // Missing Gems
    } else if(gems.exists(g => !canFuse(gem, g))) {
      0 // Incompatible Gems
    } else {
      if(b.at(xStart, yStart + 1) == b.at(xStart - 1, yStart + 1)) {
        0 // Out-of-bounds below
      } else if(b.at(xStart + width - 1, yStart + 1) == b.at(xStart + width, yStart + 1)) {
        0 // Out-of-bounds above
      } else {
        val continues = (0 until width).exists { xVal =>
          b.at(xStart + xVal, yStart + 2).contains(gems(xVal))
        }
        if(!continues) {
          1 + pendingCols
        } else {
          fuseUpDepth(b, gem, xStart, yStart + 1, width, 1 + pendingCols)
        }
      }
    }
  }

  @tailrec
  def fuseRightDepth(b: Board, gem: Gem, xStart: Int, yStart: Int, height: Int, pendingCols: Int): Int = {
    val gems = (0 until height).flatMap(yVal => b.at(xStart + 1, yStart + yVal))
    if(gems.size != height) {
      0 // Missing Gems
    } else if(gems.exists(g => !canFuse(gem, g))) {
      0 // Incompatible Gems
    } else {
      if(b.at(xStart + 1, yStart) == b.at(xStart + 1, yStart - 1)) {
        0 // Out-of-bounds below
      } else if(b.at(xStart + 1, yStart + height - 1) == b.at(xStart + 1, yStart + height)) {
        0 // Out-of-bounds above
      } else {
        val continues = (0 until height).exists { yVal =>
          b.at(xStart + 2, yStart + yVal).contains(gems(yVal))
        }
        if(!continues) {
          1 + pendingCols
        } else {
          fuseRightDepth(b, gem, xStart + 1, yStart, height, 1 + pendingCols)
        }
      }
    }
  }
}
