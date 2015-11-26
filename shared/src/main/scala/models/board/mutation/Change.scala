package models.board.mutation

import models.board.Board
import models.board.mutation.Mutation.ChangeGem

object Change {
  def apply(b: Board, m: ChangeGem) = {
    val oldGem = b.at(m.x, m.y).getOrElse(throw new IllegalStateException(s"Change attempted for empty position [${m.x}, ${m.y}]."))

    if (oldGem == m.newGem) {
      throw new IllegalStateException(s"Attempted to change unchanged gem [${m.newGem}].")
    }
    if (oldGem.id != m.newGem.id) {
      throw new IllegalStateException(s"Attempted to change [$oldGem] to gem [${m.newGem}] with a different id.")
    }
    if (oldGem.width.getOrElse(1) > m.newGem.width.getOrElse(1)) {
      throw new IllegalStateException(s"Attempted to reduce [$oldGem]'s width for [${m.newGem}].")
    }
    if (oldGem.height.getOrElse(1) > m.newGem.height.getOrElse(1)) {
      throw new IllegalStateException(s"Attempted to reduce [$oldGem]'s height for [${m.newGem}].")
    }

    for (yOffset <- 0 until m.newGem.height.getOrElse(1)) {
      for (xOffset <- 0 until m.newGem.width.getOrElse(1)) {
        val shouldSet = b.at(m.x + xOffset, m.y + yOffset) match {
          case None => true
          case Some(occupant) if occupant == m.newGem => false
          case Some(occupant) if occupant.id == m.newGem.id => true
          case Some(offender) =>
            val msg = s"Attempted to expand [${m.newGem}] to space [${m.x + xOffset}, ${m.y + yOffset}], which is occupied by [$offender]."
            throw new IllegalStateException(msg)
        }
        if (shouldSet) {
          b.set(m.x + xOffset, m.y + yOffset, Some(m.newGem))
        }
      }
    }
    m
  }
}
