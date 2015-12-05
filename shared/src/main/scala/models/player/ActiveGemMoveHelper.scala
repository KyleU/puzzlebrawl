package models.player

import models.board.mutation.Mutation.MoveGem

trait ActiveGemMoveHelper { this: Player =>
  def activeGemsLeft() = moveActiveGems(-1, 0)
  def activeGemsRight() = moveActiveGems(1, 0)
  def activeGemsStep() = moveActiveGems(0, -1)

  private[this] def moveActiveGems(xDelta: Int, yDelta: Int) = {
    if (xDelta == 0 && yDelta == 0) {
      throw new IllegalStateException("Call to move active gems without a change in position.")
    }
    val newGems = activeGems.flatMap { g =>
      val newX = g.x + xDelta
      val newY = g.y + yDelta
      if (board.isValid(newX, newY)) {
        Some(g.copy(x = newX, y = newY))
      } else {
        None
        //throw new IllegalStateException(s"Cannot move gem ${g.gem} from [${g.x}, ${g.y}] to [$newX, $newY] which is occupied by [${board.at(newX, newY)}].")
      }
    }
    if (activeGems.size == newGems.size) {
      activeGems = newGems
      Some(newGems.map(g => board.applyMutation(MoveGem(g.x, g.y, xDelta, yDelta))))
    } else {
      None
    }
  }
}
