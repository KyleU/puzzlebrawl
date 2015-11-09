package models.game

case class Gem(
  id: Int,
  color: Color,
  crash: Boolean = false,
  timer: Option[Int] = None,
  group: Option[Int] = None
)
