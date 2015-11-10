package models.game

object Board {
  sealed trait Mutation

  case class AddGem(gem: Gem, x: Int, y: Int) extends Mutation
  case class MoveGem(oldX: Int, oldY: Int, newX: Int, newY: Int) extends Mutation
  case class ChangeGem(newGem: Gem, x: Int, y: Int) extends Mutation
  case class FuseGems(x: Int, y: Int, width: Int, height: Int)
  case class RemoveGem(x: Int, y: Int) extends Mutation
}

case class Board(width: Int, height: Int) extends BoardMutationHelper {
  import Board._

  val spaces = Array.ofDim[Option[Gem]](width, height)
  for(x <- spaces; y <- x.indices) {
    x(y) = None
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
