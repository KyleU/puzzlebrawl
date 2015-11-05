package models.game

object Gem {
  sealed abstract class Color(val code: Char, val crashCode: Char)
  case object Red extends Color('r', 'R')
  case object Green extends Color('g', 'G')
  case object Blue extends Color('b', 'B')
  case object Yellow extends Color('y', 'Y')
}

case class Gem(id: Int, color: Gem.Color, crash: Boolean = false) {
  def toChar = if(crash) { color.crashCode } else { color.code }
}
