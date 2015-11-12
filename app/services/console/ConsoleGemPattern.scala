package services.console

import com.googlecode.lanterna.TextColor
import models.game.{ Gem, FuseRole }

object ConsoleGemPattern {
  def pattern(gemOpt: Option[Gem]) = gemOpt match {
    case None => (' ', ' ', TextColor.ANSI.WHITE)
    case Some(gem) if gem.timer.isDefined => (gem.timer.getOrElse(0).toString.head, gem.timer.getOrElse(0).toString.head, getColor(gem.color))
    case Some(gem) if gem.fuseRole.isDefined =>
      import ConsoleBorders._
      val color = getColor(gem.color)
      gem.fuseRole.getOrElse(throw new IllegalStateException()) match {
        case FuseRole.TopLeft => (ulCorner, horizontal, color)
        case FuseRole.Top => (horizontal, horizontal, color)
        case FuseRole.TopRight => (horizontal, urCorner, color)
        case FuseRole.Right => (' ', vertical, color)
        case FuseRole.BottomRight => (horizontal, brCorner, color)
        case FuseRole.Bottom => (horizontal, horizontal, color)
        case FuseRole.BottomLeft => (blCorner, horizontal, color)
        case FuseRole.Left => (vertical, ' ', color)
        case FuseRole.Center => (':', ':', color)
      }
    case Some(gem) => gem.crash match {
      case true => ('(', ')', getColor(gem.color))
      case false => ('[', ']', getColor(gem.color))
    }
  }

  private[this] def getColor(c: Gem.Color) = c match {
    case Gem.Red => TextColor.ANSI.RED
    case Gem.Green => TextColor.ANSI.GREEN
    case Gem.Blue => TextColor.ANSI.BLUE
    case Gem.Yellow => TextColor.ANSI.YELLOW
    case Gem.Wild => TextColor.ANSI.WHITE
  }
}
