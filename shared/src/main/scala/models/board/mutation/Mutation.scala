package models.board.mutation

import java.util.UUID

import models.gem.Gem

sealed trait Mutation

object Mutation {
  final case class AddGem(gem: Gem, x: Int, y: Int) extends Mutation
  final case class MoveGem(x: Int, y: Int, xDelta: Int, yDelta: Int) extends Mutation
  final case class MoveGems(moves: Seq[MoveGem]) extends Mutation
  final case class ChangeGem(newGem: Gem, x: Int, y: Int) extends Mutation
  final case class RemoveGem(x: Int, y: Int) extends Mutation
  final case class TargetChanged(t: UUID) extends Mutation
}
