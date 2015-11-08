package models.game

object Gem {
  sealed trait Color
  case object Red extends Color
  case object Green extends Color
  case object Blue extends Color
  case object Yellow extends Color
  case object Wild extends Color
}

case class Gem(
  id: Int,
  color: Gem.Color,
  crash: Boolean = false,
  timer: Option[Int] = None,
  group: Option[Int] = None
)
