package models.game

sealed trait Mutation

case class AddGem(gem: Gem, x: Int, y: Int) extends Mutation
case class MoveGem(gem: Gem, oldX: Int, oldY: Int, newX: Int, newY: Int) extends Mutation
case class ChangeGem(oldGem: Gem, newGem: Gem, x: Int, y: Int) extends Mutation
case class RemoveGem(gem: Gem, x: Int, y: Int) extends Mutation
