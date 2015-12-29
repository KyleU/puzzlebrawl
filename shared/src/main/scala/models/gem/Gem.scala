package models.gem

case class Gem(
    id: Int,
    color: Color = Color.Red,
    crash: Option[Boolean] = None,
    timer: Option[Int] = None,
    width: Option[Int] = None,
    height: Option[Int] = None) {
  lazy val size = width.getOrElse(1) * height.getOrElse(1)

  override def toString = {
    val crashAppend = if (crash.exists(x => x)) { ", crash" } else { "" }
    val timerAppend = timer.map(t => ", timer " + t).getOrElse("")
    val dimensionsAppend = if (width.isDefined || height.isDefined) { s" (${width.getOrElse(1)}x${height.getOrElse(1)})" } else { "" }
    s"[$id: $color$crashAppend$timerAppend$dimensionsAppend]"
  }
}
