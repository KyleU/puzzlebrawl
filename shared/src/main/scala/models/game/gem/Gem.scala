package models.game.gem

case class Gem(
  id: Int,
  color: Color,
  crash: Boolean = false,
  timer: Option[Int] = None,
  group: Option[Int] = None
)
