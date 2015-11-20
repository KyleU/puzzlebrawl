package models.game.gem

sealed trait GroupRole

object GroupRole {
  case object TopLeft extends GroupRole
  case object Top extends GroupRole
  case object TopRight extends GroupRole
  case object Right extends GroupRole
  case object BottomRight extends GroupRole
  case object Bottom extends GroupRole
  case object BottomLeft extends GroupRole
  case object Left extends GroupRole
  case object Center extends GroupRole

  def roleFor(x: Int, y: Int, width: Int, height: Int) = if (x == 0) {
    if (y == 0) {
      TopLeft
    } else if (y == height - 1) {
      BottomLeft
    } else {
      Left
    }
  } else if (x == width - 1) {
    if (y == 0) {
      TopRight
    } else if (y == height - 1) {
      BottomRight
    } else {
      Right
    }
  } else if (y == 0) {
    Top
  } else if (y == height - 1) {
    Bottom
  } else {
    Center
  }
}
