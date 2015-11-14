package models.game.gem

case class Gem(
  id: Int,
  color: Color,
  crash: Boolean = false,
  timer: Option[Int] = None,
  group: Option[Int] = None
) {
  override def toString = s"[$id: $color${if(crash) { ", crash" } else { "" }}${timer.map(t => ", timer " + t).getOrElse("")}${group.map(g => ", group " + g).getOrElse("")}]"
}
