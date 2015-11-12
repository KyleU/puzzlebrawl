package models.game.board

import models.game.gem.Gem

object Board {
  sealed trait Mutation

  case class AddGem(gem: Gem, x: Int, y: Int) extends Mutation
  case class MoveGem(oldX: Int, oldY: Int, newX: Int, newY: Int) extends Mutation
  case class ChangeGem(newGem: Gem, x: Int, y: Int) extends Mutation
  case class FuseGems(x: Int, y: Int, width: Int, height: Int)
  case class RemoveGem(x: Int, y: Int) extends Mutation
}

case class Board(width: Int, height: Int) extends BoardHelper {
  import Board._

  protected[this] val spaces = Array.ofDim[Option[Gem]](width, height)
  for(x <- 0 until width; y <- 0 until height) {
    spaces(x)(y) = None
  }

  def at(x: Int, y: Int) = if(x < 0 || x > width - 1 || y < 0 || y > height - 1) {
    None
  } else {
    spaces(x)(y)
  }

  def mapSpaces[T](f: (Option[Gem], Int, Int) => Seq[T]) = for(x <- 0 until width; y <- 0 until height) yield {
    f(at(x, y), x, y)
  }

  def mapGems[T](f: (Gem, Int, Int) => Seq[T]) = mapSpaces {
    case (Some(gem), x, y) => f(gem, x, y)
    case _ => Seq.empty
  }

  def add(gem: Gem, x: Int, y: Int) = applyMutation(AddGem(gem, x, y))

  def clear() = for(x <- spaces; y <- x.indices) { x(y) = None }

  def drop(gem: Gem, x: Int) = {
    val col = spaces(x)
    val yOpt = col.indices.reverseIterator.find(i => col(i).isDefined) match {
      case Some(yMatch) if yMatch == height - 1 => None
      case Some(yMatch) => Some(yMatch + 1)
      case None => Some(0)
    }
    yOpt.foreach(y => applyMutation(AddGem(gem, x, y)))
  }
}
