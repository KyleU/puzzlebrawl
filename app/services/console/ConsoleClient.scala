package services.console

import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.{ TerminalPosition, TextCharacter, TextColor }
import models.game.{ FuseRole, Board, Gem, GemStream }

class ConsoleClient {
  var boards = Seq.empty[(Board, Int, Int)]

  val gemStream = GemStream(0)

  val screen = {
    val factory = new DefaultTerminalFactory()
    factory.setSuppressSwingTerminalFrame(true)
    val terminal = factory.createTerminal()
    val s = new TerminalScreen(terminal)
    s.startScreen()
    s.clear()
    s
  }

  val graphics = screen.newTextGraphics()

  def stop() = screen.stopScreen()

  def add(b: Board, x: Int, y: Int) = {
    boards = boards :+ ((b, x, y))
  }

  def render() = {
    if(boards.isEmpty) {
      throw new IllegalStateException("No boards in client.")
    }

    boards.foreach { b =>
      b._1.spaces.indices.foreach { x =>
        val col = b._1.spaces(x)
        col.indices.foreach { y =>
          val pattern = col(y) match {
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

          val targetX = b._2 + (x * 2)
          val targetY = b._3 + (b._1.height - y - 1)

          screen.setCharacter(targetX, targetY, new TextCharacter(pattern._1, pattern._3, TextColor.ANSI.BLACK))
          screen.setCharacter(targetX + 1, targetY, new TextCharacter(pattern._2, pattern._3, TextColor.ANSI.BLACK))
        }
      }
    }

    val inputY = boards.map( b => b._1.height + b._3).max + 2
    graphics.putString(0, inputY, "Awaiting Input: ")
    screen.setCursorPosition(new TerminalPosition(16, inputY))
    screen.refresh()

    screen.refresh()
  }

  private[this] def getColor(c: Gem.Color) = c match {
    case Gem.Red => TextColor.ANSI.RED
    case Gem.Green => TextColor.ANSI.GREEN
    case Gem.Blue => TextColor.ANSI.BLUE
    case Gem.Yellow => TextColor.ANSI.YELLOW
    case Gem.Wild => TextColor.ANSI.WHITE
  }
}
