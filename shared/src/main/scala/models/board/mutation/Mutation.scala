package models.board.mutation

import models.gem.Gem

sealed trait Mutation

object Mutation {
  case class AddGem(gem: Gem, x: Int, y: Int) extends Mutation
  case class MoveGem(x: Int, y: Int, xDelta: Int, yDelta: Int) extends Mutation
  case class MoveGems(moves: Seq[MoveGem]) extends Mutation
  case class ChangeGem(newGem: Gem, x: Int, y: Int) extends Mutation
  case class RemoveGem(x: Int, y: Int, score: Option[Int] = None) extends Mutation
}
