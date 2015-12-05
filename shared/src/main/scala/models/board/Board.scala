package models.board

import models.board.mutation._
import models.board.mutation.Mutation._
import models.gem.Gem

case class Board(key: String, width: Int, height: Int) extends BoardHelper {
  protected[this] val spaces = Array.ofDim[Option[Gem]](width, height)
  for (x <- 0 until width; y <- 0 until height) {
    spaces(x)(y) = None
  }
  def getSpacesCopy = spaces.map(x => x.map(y => y))

  def at(x: Int, y: Int) = if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
    None
  } else {
    spaces(x)(y)
  }

  def isValid(x: Int, y: Int) = if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
    false
  } else {
    spaces(x)(y).isEmpty
  }

  def set(x: Int, y: Int, gem: Option[Gem]) = if (x < 0 || x > width - 1) {
    throw new IllegalArgumentException(s"Index [$x] is outside of width [$width].")
  } else if (y < 0 || y > height - 1) {
    throw new IllegalArgumentException(s"Index [$y] is outside of height [$height].")
  } else {
    spaces(x)(y) = gem
  }

  def mapSpaces[T](f: (Option[Gem], Int, Int) => Seq[T]) = for (y <- 0 until height; x <- 0 until width) yield f(at(x, y), x, y)

  def mapGems[T](f: (Gem, Int, Int) => Seq[T]) = {
    val encounteredGems = collection.mutable.HashSet.empty[Gem]
    mapSpaces {
      case (Some(gem), x, y) => if (encounteredGems.contains(gem)) {
        Seq.empty
      } else {
        encounteredGems += gem
        f(gem, x, y)
      }
      case _ => Seq.empty
    }
  }

  def applyMutation(m: Mutation): Mutation = m match {
    case ag: AddGem => Add(this, ag)
    case mg: MoveGem => Move(this, mg)
    case cg: ChangeGem => Change(this, cg)
    case rg: RemoveGem => Remove(this, rg)
  }

  def cloneTo(board: Board) = for (y <- 0 until height) {
    for (x <- 0 until width) {
      board.set(x, y, at(x, y))
    }
  }

  def clear() = mapGems((gem, x, y) => Seq(applyMutation(RemoveGem(x, y))))
}
