package models.game.gem

object Color {
  case object Red extends Color('r')
  case object Green extends Color('g')
  case object Blue extends Color('b')
  case object Yellow extends Color('y')
  case object Wild extends Color('w')
  val allColors = Seq(Red, Green, Blue, Yellow, Wild)
  def fromChar(c: Char) = allColors.find(_ == c).getOrElse(throw new IllegalArgumentException(s"Invalid color [$c]."))
}

sealed abstract class Color(val charVal: Char)
