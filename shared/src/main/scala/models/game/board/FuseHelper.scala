package models.game.board

import models.game.board.mutation.Mutation
import models.game.board.mutation.Mutation.{ RemoveGem, ChangeGem }
import models.game.gem.Gem

trait FuseHelper { this: Board =>
  def fuse(): Seq[Seq[Mutation]] = mapGems { (gem, x, y) =>
    if (gem.crash || gem.timer.isDefined) {
      Seq.empty
    } else {
      val (width, height) = largestSize(gem, x, y)

      if (width > 1 && height > 1 && (width > gem.width.getOrElse(1) || height > gem.height.getOrElse(1))) {
        val removals = (0 until height).flatMap { yOffset =>
          (0 until width).flatMap { xOffset =>
            val testGem = at(x + xOffset, y + yOffset)
            if (testGem.isEmpty) {
              None
            } else if (testGem.contains(gem)) {
              None
            } else {
              Some(applyMutation(RemoveGem(x + xOffset, y + yOffset)))
            }
          }
        }

        removals :+ applyMutation(ChangeGem(gem.copy(width = Some(width), height = Some(height)), x, y))
      } else {
        Seq.empty
      }
    }
  }

  private[this] def largestSize(gem: Gem, x: Int, y: Int) = {
    val checkedDimensions = collection.mutable.HashSet.empty[(Int, Int)]

    def expand(gem: Gem, x: Int, y: Int, width: Int, height: Int): (Int, Int) = {
      checkedDimensions += (width -> height)

      val upDepth = FuseDepthHelper.fuseUpDepth(this, gem, x, y + height - 1, width, 0)
      val upDim = width -> (height + upDepth)
      val upResult = if (upDepth == 0 || checkedDimensions(upDim)) {
        upDim
      } else {
        expand(gem, x, y, upDim._1, upDim._2)
      }
      val upArea = if (upResult._1 == 1 || upResult._2 == 1) { 1 } else { upResult._1 * upResult._2 }

      val rightDepth = FuseDepthHelper.fuseRightDepth(this, gem, x + width - 1, y, height, 0)
      val rightDim = (width + rightDepth) -> height
      val rightResult = if (rightDepth == 0 || checkedDimensions(rightDim)) {
        rightDim
      } else {
        expand(gem, x, y, rightDim._1, rightDim._2)
      }
      val rightArea = if (rightResult._1 == 1 || rightResult._2 == 1) { 1 } else { rightResult._1 * rightResult._2 }

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
