package models.game.gem

case class Gem(
    id: Int,
    color: Color,
    crash: Boolean = false,
    timer: Option[Int] = None,
    width: Option[Int] = None,
    height: Option[Int] = None
) {
  override def toString = {
    val crashAppend = if (crash) { ", crash" } else { "" }
    val timerAppend = timer.map(t => ", timer " + t).getOrElse("")
    val dimensionsAppend = if (width.isDefined || height.isDefined) { s" (${width.getOrElse(1)}x${height.getOrElse(1)})" } else { "" }
    s"[$id: $color$crashAppend$timerAppend$dimensionsAppend]"
  }
}
