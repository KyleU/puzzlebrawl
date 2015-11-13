package services.console

import com.googlecode.lanterna.TextColor
import models.game.gem.{ Color, Gem }

object ConsoleGemPattern {
  def pattern(gemOpt: Option[Gem]) = gemOpt match {
    case None => (' ', ' ', TextColor.ANSI.WHITE)
    case Some(gem) if gem.timer.isDefined => (gem.timer.getOrElse(0).toString.head, gem.timer.getOrElse(0).toString.head, getColor(gem.color))
    case Some(gem) if gem.group.isDefined =>
      import ConsoleBorders._
      val color = getColor(gem.color)
//      gem.group.map(_._2).getOrElse(throw new IllegalStateException()) match {
//        case GroupRole.TopLeft => (ulCorner, horizontal, color)
//        case GroupRole.Top => (horizontal, horizontal, color)
//        case GroupRole.TopRight => (horizontal, urCorner, color)
//        case GroupRole.Right => (' ', vertical, color)
//        case GroupRole.BottomRight => (horizontal, brCorner, color)
//        case GroupRole.Bottom => (horizontal, horizontal, color)
//        case GroupRole.BottomLeft => (blCorner, horizontal, color)
//        case GroupRole.Left => (vertical, ' ', color)
//        case GroupRole.Center => (' ', ' ', color)
//      }
      (horizontal, horizontal, color) // TODO Fix

    case Some(gem) => gem.crash match {
      case true => ('(', ')', getColor(gem.color))
      case false => ('[', ']', getColor(gem.color))
    }
  }

  private[this] def getColor(color: Color) = color match {
    case Color.Red => TextColor.ANSI.RED
    case Color.Green => TextColor.ANSI.GREEN
    case Color.Blue => TextColor.ANSI.BLUE
    case Color.Yellow => TextColor.ANSI.YELLOW
    case Color.Wild => TextColor.ANSI.WHITE
  }
}
