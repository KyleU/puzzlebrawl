package models.player

trait ActiveGemMoveHelper { this: Player =>
  def activeGemsLeft() = {
    val ok = !activeGems.exists(g => !board.isValid(g.x - 1, g.y))
    if (ok) { moveActiveGems(-1, 0) }
  }

  def activeGemsRight() = {
    val ok = !activeGems.exists(g => !board.isValid(g.x + 1, g.y))
    if (ok) { moveActiveGems(1, 0) }
  }

  def activeGemsStep() = {
    activeGems.foreach { g =>
      if (g.y == 0) {
        throw new IllegalStateException(s"Gem ${g.gem} cannot be dropped to [${g.x}, ${g.y - 1}].")
      }
      if (!board.isValid(g.x, g.y - 1)) {
        throw new IllegalStateException(s"Gem ${g.gem} cannot be moved to [${g.x}, ${g.y - 1}], which is occupied by ${board.at(g.x, g.y - 1)}.")
      }
    }
    moveActiveGems(0, -1)
  }

  private[this] def moveActiveGems(xDelta: Int, yDelta: Int) = {
    val newGems = activeGems.map { g =>
      val newX = g.x + xDelta
      val newY = g.y + yDelta
      if (board.isValid(newX, newY)) {
        g.copy(x = newX, y = newY)
      } else {
        throw new IllegalStateException(s"Cannot move gem ${g.gem} from [${g.x}, ${g.y}] to [$newX, $newY] which is occupied by [${board.at(newX, newY)}].")
      }
    }
    activeGems = newGems
  }
}
