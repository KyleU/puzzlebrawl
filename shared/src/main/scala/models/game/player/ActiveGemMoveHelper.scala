package models.game.player

trait ActiveGemMoveHelper { this: Player =>
  def activeGemsLeft() = {
    val ok = !activeGems.exists(g => if(g.x == 0) { true } else { board.at(g.x - 1, g.y).isDefined })
    if(ok) { moveActiveGems(-1, 0) }
  }

  def activeGemsRight() = {
    val ok = !activeGems.exists(g => if(g.x == board.width - 1) { true } else { board.at(g.x + 1, g.y).isDefined })
    if(ok) { moveActiveGems(1, 0) }
  }

  def activeGemsStep() = {
    activeGems.foreach { g =>
      if(g.y == 0) {
        throw new IllegalStateException(s"Gem ${g.gem} cannot be dropped to [${g.x}, ${g.y - 1}].")
      }
      board.at(g.x, g.y - 1) match {
        case Some(o) => throw new IllegalStateException(s"Gem ${g.gem} cannot be moved to [${g.x}, ${g.y - 1}], which is occupied by $o.")
        case None => // ok
      }
    }
    moveActiveGems(0, -1)
  }

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
}
