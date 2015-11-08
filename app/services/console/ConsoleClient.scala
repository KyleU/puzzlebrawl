package services.console

import com.googlecode.lanterna.{TerminalPosition, TextColor, TextCharacter}
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import models.game.{Gem, Board}

object ConsoleClient {
  private val fgColor = TextColor.ANSI.WHITE
  private val bgColor = TextColor.ANSI.BLACK

  def main(args: Array[String]) {
    val c = new ConsoleClient()

    val b = Board(6, 12)
    b.add(Gem(0, Gem.Red), 0, 0)
    b.add(Gem(1, Gem.Blue), 1, 1)
    b.add(Gem(2, Gem.Blue, crash = true), 2, 2)
    b.add(Gem(3, Gem.Green, crash = true), 3, 2)
    b.add(Gem(4, Gem.Green), 4, 1)
    b.add(Gem(5, Gem.Yellow), 5, 0)

    ConsoleBorders.render(c, 0, 0, b.width, b.height, fgColor, bgColor)
    c.render(b, 1, 1)

    c.graphics.putString(0, b.height + 3, "Awaiting Input: ")
    c.screen.setCursorPosition(new TerminalPosition(16, b.height + 3))
    c.screen.refresh()

    c.screen.readInput()

    c.stop()
  }
}

class ConsoleClient {
  import ConsoleClient._

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

  def render(b: Board, boardX: Int, boardY: Int) = b.spaces.indices.foreach { x =>
    val col = b.spaces(x)
    col.indices.foreach { y =>
      val pattern = col(y) match {
        case None => (' ', ' ', TextColor.ANSI.WHITE)
        case Some(gem) => gem.crash match {
          case true => ('(', ')', getColor(gem.color))
          case false => ('[', ']', getColor(gem.color))
        }
      }

      val targetX = boardX + (x * 2)
      val targetY = boardY + (b.height - y - 1)

      screen.setCharacter(targetX, targetY, new TextCharacter(pattern._1, pattern._3, bgColor))
      screen.setCharacter(targetX + 1, targetY, new TextCharacter(pattern._2, pattern._3, bgColor))
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
