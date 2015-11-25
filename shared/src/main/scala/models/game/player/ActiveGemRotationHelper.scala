package models.game.player

import models.game.gem.GemLocation

trait ActiveGemRotationHelper { this: Player =>
  def activeGemsClockwise() = activeGems match {
    case Seq(a, b) =>
      val delta = (b.x - a.x, b.y - a.y) match {
        case (1, 0) => -1 -> -1 // To the right
        case (0, -1) => -1 -> 1 // Below
        case (-1, 0) => 1 -> 1 // To the left
        case (0, 1) => 1 -> -1 // Above
        case (x, y) => throw new IllegalStateException(s"Unable to rotate active gems with unknown offset [$x, $y].")
      }
      val newB = b.copy(x = b.x + delta._1, y = b.y + delta._2)
      setIfPossible(a, newB)
    case _ => throw new IllegalStateException(s"There are [${activeGems.size}] active gems, but [2] are needed.")
  }

  def activeGemsCounterClockwise() = activeGems match {
    case Seq(a, b) =>
      val delta = (b.x - a.x, b.y - a.y) match {
        case (1, 0) => -1 -> 1 // To the right
        case (0, -1) => 1 -> 1 // Below
        case (-1, 0) => 1 -> -1 // To the left
        case (0, 1) => -1 -> -1 // Above
        case (x, y) => throw new IllegalStateException(s"Unable to rotate active gems with unknown offset [$x, $y].")
      }
      val newB = b.copy(x = b.x + delta._1, y = b.y + delta._2)
      setIfPossible(a, newB)
    case _ => throw new IllegalStateException(s"There are [${activeGems.size}] active gems, but [2] are needed.")
  }

  private[this] def setIfPossible(a: GemLocation, newB: GemLocation) = board.at(newB.x, newB.y) match {
    case None => if (newB.x < 0) {
      val xDelta = -newB.x
      val newGems = Seq(a.copy(x = a.x + xDelta), newB.copy(x = newB.x + xDelta))
      if(newGems.exists(g => board.at(g.x, g.y).isDefined)) {
        // Blocked right, no op
      } else {
        activeGems = newGems
      }
    } else if (newB.x >= board.width) {
      val xDelta = newB.x - board.width - 1
      val newGems = Seq(a.copy(x = a.x + xDelta), newB.copy(x = newB.x + xDelta))
      if(newGems.exists(g => board.at(g.x, g.y).isDefined)) {
        // Blocked left, no op
      } else {
        activeGems = newGems
      }
    } else if (newB.y >= board.height) {
      val yDelta = board.height - 1 - newB.y
      val newGems = Seq(a.copy(y = a.y + yDelta), newB.copy(y = newB.y + yDelta))
      if(newGems.exists(g => board.at(g.x, g.y).isDefined)) {
        // Blocked below, no op
      } else {
        activeGems = newGems
      }
    } else if (newB.y < 0) {
      // Outside bottom, no op
    } else if(board.at(newB.x, newB.y).isDefined) {
      // Occupied, no op for now.
    } else {
      activeGems = Seq(a, newB)
    }
    case Some(occupant) => // Occupied, no op
  }
}
