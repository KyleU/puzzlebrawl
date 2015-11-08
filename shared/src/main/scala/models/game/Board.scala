package models.game

import models.game.Board.RemoveGem

object Board {
  sealed trait Mutation

  case class AddGem(gem: Gem, x: Int, y: Int) extends Mutation
  case class MoveGem(gem: Gem, oldX: Int, oldY: Int, newX: Int, newY: Int) extends Mutation
  case class ChangeGem(oldGem: Gem, newGem: Gem, x: Int, y: Int) extends Mutation
  case class RemoveGem(gem: Gem, x: Int, y: Int) extends Mutation
}

case class Board(width: Int, height: Int) {
  val spaces = Array.ofDim[Option[Gem]](width, height)
  for(x <- spaces; y <- x.indices) {
    x(y) = None
  }

  def add(gem: Gem, x: Int, y: Int) = applyMutation(Board.AddGem(gem, x, y))

  def applyMutation(m: Board.Mutation) = m match {
    case ag: Board.AddGem => applyAdd(ag)
    case mg: Board.MoveGem => applyMove(mg)
    case cg: Board.ChangeGem => applyChange(cg)
    case rg: Board.RemoveGem => applyRemove(rg)
  }

  def forEach(f: (Option[Gem], Int, Int) => Option[Board.Mutation]) = (height - 1 to  0).foreach { y =>
    (width - 1 to 0).foreach { x =>
      val space = spaces(x)(y)
      f(space, x, y).foreach(applyMutation)
    }
  }

  def forEachGem(f: (Gem, Int, Int) => Option[Board.Mutation]) = {
    forEach((space: Option[Gem], x: Int, y: Int) => space.flatMap(gem => f(gem, x, y)))
  }

  private[this] def applyAdd(m: Board.AddGem) = spaces(m.x)(m.y) match {
    case Some(offender) => throw new IllegalStateException(s"Attempt to add [${m.gem}] to [${m.x}, ${m.y}], which is occupied by [$offender].")
    case None => spaces(m.x)(m.y) = Some(m.gem)
  }

  private[this] def applyChange(m: Board.ChangeGem) = {
    if(m.oldGem == m.newGem) {
      throw new IllegalStateException(s"Attempted to change unchanged gem [${m.newGem}].")
    }
    if(m.oldGem.id != m.newGem.id) {
      throw new IllegalStateException(s"Attempted to change [${m.oldGem}] to gem [${m.newGem}] with a different id.")
    }
    spaces(m.x)(m.y) = Some(m.newGem)
  }

  private[this] def applyMove(m: Board.MoveGem) = spaces(m.newX)(m.newY) match {
    case Some(offender) =>
      val msg = s"Attempted to move [${m.gem}] to [${m.newX}, ${m.newY}], which is occupied by [$offender]."
      throw new IllegalStateException(msg)
    case None =>
      spaces(m.oldX)(m.oldY) = None
      spaces(m.newX)(m.newY) = Some(m.gem)
  }

  private[this] def applyRemove(m: RemoveGem) = {
    if(!spaces(m.x)(m.y).contains(m.gem)) {
      throw new IllegalStateException(s"Attempt to remove [${m.gem}] from empty location [${m.x}, ${m.y}].")
    }
    spaces(m.x)(m.y) = None
  }
}
