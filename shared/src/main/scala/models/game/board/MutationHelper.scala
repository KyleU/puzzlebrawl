package models.game.board

import models.game.board.Board._
import models.game.gem.Gem

import scala.annotation.tailrec

trait MutationHelper { this: Board =>
  def applyMutations(mutations: Seq[Mutation]) = mutations.foreach(applyMutation)

  def applyMutation(m: Mutation): Unit = m match {
    case ag: AddGem => applyAdd(ag)
    case mg: MoveGem => applyMove(mg)
    case cg: ChangeGem => applyChange(cg)
    case rg: RemoveGem => applyRemove(rg)
  }

  private[this] def applyAdd(m: AddGem) = {
    val me = Some(m.gem)
    for(y <- 0 until m.gem.height.getOrElse(1)) {
      for(x <- 0 until m.gem.width.getOrElse(1)) {
        at(m.x + x, m.y + y) match {
          case Some(g) => throw new IllegalStateException(s"Attempt to add index [$x, $y] of [${m.gem}] to [${m.x}, ${m.y}], which is occupied by [$g].")
          case None => set(m.x + x, m.y + y, me)
        }
      }
    }
  }

  private[this] def applyChange(m: ChangeGem) = {
    val oldGem = at(m.x, m.y).getOrElse(throw new IllegalStateException(s"Change attempted for empty position [${m.x}, ${m.y}]."))

    if(oldGem == m.newGem) {
      throw new IllegalStateException(s"Attempted to change unchanged gem [${m.newGem}].")
    }
    if(oldGem.id != m.newGem.id) {
      throw new IllegalStateException(s"Attempted to change [$oldGem] to gem [${m.newGem}] with a different id.")
    }
    if(oldGem.width.getOrElse(1) > m.newGem.width.getOrElse(1)) {
      throw new IllegalStateException(s"Attempted to reduce [$oldGem]'s width for [${m.newGem}].")
    }
    if(oldGem.height.getOrElse(1) > m.newGem.height.getOrElse(1)) {
      throw new IllegalStateException(s"Attempted to reduce [$oldGem]'s height for [${m.newGem}].")
    }

    for(yOffset <- 0 until m.newGem.height.getOrElse(1)) {
      for(xOffset <- 0 until m.newGem.width.getOrElse(1)) {
        val shouldSet = at(m.x + xOffset, m.y + yOffset) match {
          case None => true
          case Some(occupant) if occupant == m.newGem => false
          case Some(occupant) if occupant.id == m.newGem.id => true
          case Some(offender) =>
            val msg = s"Attempted to expand [${m.newGem}] to space [${m.x + xOffset}, ${m.y + yOffset}], which is occupied by [$offender]."
            throw new IllegalStateException(msg)
        }
        if(shouldSet) {
          set(m.x + xOffset, m.y + yOffset, Some(m.newGem))
        }
      }
    }
  }

  private[this] def applyMove(m: MoveGem) = {
    val gem = at(m.x, m.y).getOrElse(throw new IllegalStateException(s"Move attempted from empty position [${m.x}, ${m.y}]."))
    val (startX, startY) = startIndexFor(gem, m.x, m.y)
    for(y <- 0 until gem.height.getOrElse(1)) {
      for(x <- 0 until gem.width.getOrElse(1)) {
        val src = (startX + x, startY + y)
        val tgt = (x, y)
        set(src._1, src._2, None)
        set(src._1 + m.xDelta, src._2 + m.yDelta, Some(gem))
      }
    }
  }

  private[this] def applyRemove(m: RemoveGem) = {
    val gem = at(m.x, m.y).getOrElse(throw new IllegalStateException(s"Remove attempted from empty position [${m.x}, ${m.y}]."))
    val (startX, startY) = startIndexFor(gem, m.x, m.y)

    for(y <- 0 until gem.height.getOrElse(1)) {
      for(x <- 0 until gem.width.getOrElse(1)) {
        at(startX + x, startY + y) match {
          case Some(g) => set(startX + x, startY + y, None)
          case None => throw new IllegalStateException(s"Remove attempted for [$gem] from empty position [$startX, $startY] at index [$x, $y].")
        }
      }
    }
  }

  @tailrec
  private[this] def startIndexFor(gem: Gem, x: Int, y: Int): (Int, Int) = {
    if(at(x - 1, y).contains(gem)) {
      startIndexFor(gem, x - 1, y)
    } else {
      if(at(x, y - 1).contains(gem)) {
        startIndexFor(gem, x, y - 1)
      } else {
        (x, y)
      }
    }
  }
}
