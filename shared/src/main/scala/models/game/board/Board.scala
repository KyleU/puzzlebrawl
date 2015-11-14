package models.game.board

import models.game.gem.Gem

object Board {
  sealed trait Mutation

  case class AddGem(gem: Gem, x: Int, y: Int) extends Mutation
  case class MoveGem(oldX: Int, oldY: Int, newX: Int, newY: Int) extends Mutation
  case class ChangeGem(newGem: Gem, x: Int, y: Int) extends Mutation
  case class FuseGems(groupId: Int, x: Int, y: Int, width: Int, height: Int) extends Mutation
  case class RemoveGem(x: Int, y: Int) extends Mutation

  def withKey(key: String) = ofSize(key, 6, 12)
  def ofSize(key: String, width: Int, height: Int) = {
    val spaces = Array.ofDim[Option[Gem]](width, height)
    for(x <- 0 until width; y <- 0 until height) {
      spaces(x)(y) = None
    }
    Board(key, spaces)
  }
}

case class Board(key: String, spaces: Array[Array[Option[Gem]]]) extends BoardHelper {
  import Board._

  val width = spaces.length
  val height = spaces.headOption.map(_.length).getOrElse(0)

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

  def decrementTimers() = mapGems { (gem, x, y) =>
    gem.timer match {
      case Some(v) =>
        val msg = ChangeGem(gem.copy(timer = if(v == 1) { None } else { Some(v - 1) }), x, y)
        applyMutation(msg)
        Seq(msg)
      case None => Seq.empty
    }
  }
}
