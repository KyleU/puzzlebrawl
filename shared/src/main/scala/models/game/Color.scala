package models.game

sealed trait Color

object Color {
  case object Red extends Color
  case object Green extends Color
  case object Blue extends Color
  case object Yellow extends Color
  case object Wild extends Color
}
