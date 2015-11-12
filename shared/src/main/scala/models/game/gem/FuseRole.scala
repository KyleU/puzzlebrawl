package models.game.gem

sealed trait FuseRole

object FuseRole {
  case object TopLeft extends FuseRole
  case object Top extends FuseRole
  case object TopRight extends FuseRole
  case object Right extends FuseRole
  case object BottomRight extends FuseRole
  case object Bottom extends FuseRole
  case object BottomLeft extends FuseRole
  case object Left extends FuseRole
  case object Center extends FuseRole

  def roleFor(x: Int, y: Int, width: Int, height: Int) = if(x == 0) {
    if (y == 0) {
      TopLeft
    } else if (y == height - 1) {
      BottomLeft
    } else {
      Left
    }
  } else if(x == width - 1) {
    if (y == 0) {
      TopRight
    } else if (y == height - 1) {
      BottomRight
    } else {
      Right
    }
  } else if(y == 0) {
    Top
  } else if(y == height - 1) {
    Bottom
  } else {
    Center
  }
}
