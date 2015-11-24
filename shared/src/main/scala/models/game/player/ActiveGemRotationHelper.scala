package models.game.player

import models.game.gem.GemLocation

trait ActiveGemRotationHelper { this: Player =>
  def activeGemsClockwise() = setActiveGems(activeGems match {
    case Seq(a, b) =>
      val coords = (b.x - a.x, b.y - a.y) match {
        case (1, 0) => -1 -> -1 // To the right
        case (0, -1) => -1 -> 1 // Below
        case (-1, 0) => 1 -> 1 // To the left
        case (0, 1) => 1 -> -1 // Above
        case (x, y) => throw new IllegalStateException(s"Unable to rotate active gems with unknown offset [$x, $y].")
      }
      Seq(a, b.copy(x = b.x + coords._1, y = b.y + coords._2))
    case _ => throw new IllegalStateException(s"There are [${activeGems.size}] active gems, but [2] are needed.")
  })

  def activeGemsCounterClockwise() = setActiveGems(activeGems match {
    case Seq(a, b) =>
      val coords = (b.x - a.x, b.y - a.y) match {
        case (1, 0) => -1 -> 1 // To the right
        case (0, -1) => 1 -> 1 // Below
        case (-1, 0) => 1 -> -1 // To the left
        case (0, 1) => -1 -> -1 // Above
        case (x, y) => throw new IllegalStateException(s"Unable to rotate active gems with unknown offset [$x, $y].")
      }
      Seq(a, b.copy(x = b.x + coords._1, y = b.y + coords._2))
    case _ => throw new IllegalStateException(s"There are [${activeGems.size}] active gems, but [2] are needed.")
  })

  private[this] def moveActiveGems(xDelta: Int, yDelta: Int) = {
    val newGems = activeGems.map { g =>
      val newX = g.x + xDelta
      val newY = g.y + yDelta

      if(newX < 0 || newX >= board.width) {
        throw new IllegalStateException(s"Cannot move gem ${g.gem} from [${g.x}, ${g.y}] to [$newX, $newY].")
      }
      if(newY < 0 || newY >= board.height) {
        throw new IllegalStateException(s"Cannot move gem ${g.gem} from [${g.x}, ${g.y}] to [$newX, $newY].")
      }
      board.at(newX, newY) match {
        case Some(o) => throw new IllegalStateException(s"Cannot move gem ${g.gem} from [${g.x}, ${g.y}] to [$newX, $newY] which is occupied by [$o].")
        case None => g.copy(x = newX, y = newY)
      }
    }
    activeGems = newGems
  }

  private[this] def setActiveGems(newActive: Seq[GemLocation]) = {
    val minX = newActive.map(_.x).min
    val maxX = newActive.map(_.x).max
    val xAdjusted = if(minX < 0) {
      newActive.map(g => g.copy(x = g.x - minX))
    } else if(maxX >= board.width) {
      newActive.map(g => g.copy(x = g.x + (maxX - board.width - 1)))
    } else {
      newActive
    }

    val maxY = newActive.map(_.y).max
    val yAdjusted = if(maxY >= board.height) {
      xAdjusted.map(g => g.copy(y = g.y + (maxY - board.height - 1)))
    } else {
      xAdjusted
    }
    activeGems = yAdjusted
  }
}
