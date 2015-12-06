package models.player

import models.board.mutation.Mutation.{ MoveGems, MoveGem }

trait ActiveGemMoveHelper { this: Player =>
  def activeGemsLeft() = moveActiveGems(-1, 0)
  def activeGemsRight() = moveActiveGems(1, 0)
  def activeGemsStep() = moveActiveGems(0, -1)

  private[this] def moveActiveGems(xDelta: Int, yDelta: Int) = {
    if (xDelta == 0 && yDelta == 0) {
      throw new IllegalStateException("Call to move active gems without a change in position.")
    }
    val valid = !activeGems.exists(g => !board.isValid(g.x + xDelta, g.y + yDelta, activeGems))

    if (valid) {
      val ret = board.applyMutation(MoveGems(activeGems.map(g => MoveGem(g.x, g.y, xDelta, yDelta))))
      activeGems = activeGems.map(g => g.copy(x = g.x + xDelta, y = g.y + yDelta))
      Some(ret)
    } else {
      None
    }
  }
}
