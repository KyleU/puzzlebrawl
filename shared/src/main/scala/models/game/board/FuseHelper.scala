package models.game.board

import models.game.board.Board.{ ChangeGem, RemoveGem }
import models.game.gem.Gem

trait FuseHelper { this: Board =>
  def fuse() = mapGems { (gem, x, y) =>
    val (width, height) = largestSize(gem, x, y)

    if(width > gem.width.getOrElse(1) || height > gem.height.getOrElse(1)) {
      val encounteredGems = collection.mutable.HashSet.empty[Int]
      val removals = (0 until height).flatMap { yOffset =>
        (0 until width).flatMap { xOffset =>
          val testGem = at(x + xOffset, y + yOffset)
          if(testGem.isEmpty) {
            None
          } else if(testGem.contains(gem)) {
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

  private[this] def largestSize(gem: Gem, x: Int, y: Int) = {
    val checkedDimensions = collection.mutable.HashSet.empty[(Int, Int)]

    def expand(gem: Gem, x: Int, y: Int, width: Int, height: Int): (Int, Int) = {
      checkedDimensions += (width -> height)

      val upDepth = FuseDepthHelper.fuseUpDepth(this, gem, x, y + height, width)
      val upDim = width -> (height + upDepth)
      val upResult = if (upDepth == 0 || checkedDimensions(upDim)) {
        upDim
      } else {
        expand(gem, x, y, upDim._1, upDim._2)
      }
      val upArea = if(upResult._1 == 1 || upResult._2 == 1) { 1 } else { upResult._1 * upResult._2 }

      val rightDepth = FuseDepthHelper.fuseRightDepth(this, gem, x + width, y, height)
      val rightDim = (width + rightDepth) -> height
      val rightResult = if (rightDepth == 0 || checkedDimensions(rightDim)) {
        rightDim
      } else {
        expand(gem, x, y, rightDim._1, rightDim._2)
      }
      val rightArea = if(rightResult._1 == 1 || rightResult._2 == 1) { 1 } else { rightResult._1 * rightResult._2 }

      if (upArea > rightArea) {
        upResult
      } else {
        rightResult
      }
    }

    val ret = expand(gem, x, y, gem.width.getOrElse(1), gem.height.getOrElse(1))
    ret
  }
}
