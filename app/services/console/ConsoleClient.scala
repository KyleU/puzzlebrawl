package services.console

import java.util.Random

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.{TerminalPosition, TextColor, TextCharacter}
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import models.game.{GemStream, Color, Board}

import scala.annotation.tailrec

object ConsoleClient {
  private val fgColor = TextColor.ANSI.WHITE
  private val bgColor = TextColor.ANSI.BLACK
  private val r = new Random()

  @tailrec
  private def processInput(input: KeyStroke, client: ConsoleClient): Unit = {
    val ret = input.getCharacter match {
      case x if x == 'q' || x == '\n' || (x == 'd' && input.isCtrlDown) => false
      case x if x == ' ' =>
        client.boards.foreach { b =>
          b._1.drop(client.gemStream.next, r.nextInt(b._1.width))
        }
        client.render()
        true
      case _ => true
    }
    if(ret) {
      processInput(client.screen.readInput(), client)
    }
  }


  def main(args: Array[String]) {
    val client = new ConsoleClient()

    val rows = Math.floor(client.screen.getTerminalSize.getRows / 15.toDouble).toInt
    val cols = Math.floor(client.screen.getTerminalSize.getColumns / 15.toDouble).toInt

    val boards = (0 until rows).flatMap { boardY =>
      (0 until cols).map { boardX =>
        val board = Board(6, 12)
        ConsoleBorders.render(client, 1 + (15 * boardX), 1 + (15 * boardY), board.width, board.height, fgColor, bgColor)

        (0 until 20).foreach { i =>
          board.drop(client.gemStream.next, r.nextInt(board.width))
        }

        client.add(board, 2 + (15 * boardX), 2 + (15 * boardY))

        board
      }
    }

    client.render()

    client.graphics.putString(0, ((boards.head.height + 3) * rows) + 1, "Awaiting Input: ")
    client.screen.setCursorPosition(new TerminalPosition(16, ((boards.head.height + 3) * rows) + 1))
    client.screen.refresh()

    processInput(client.screen.readInput(), client)

    client.stop()
  }
}

class ConsoleClient {
  import ConsoleClient._

  private var boards = Seq.empty[(Board, Int, Int)]

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
    boards.foreach { b =>
      b._1.spaces.indices.foreach { x =>
        val col = b._1.spaces(x)
        col.indices.foreach { y =>
          val pattern = col(y) match {
            case None => (' ', ' ', TextColor.ANSI.WHITE)
            case Some(gem) => gem.crash match {
              case true => ('(', ')', getColor(gem.color))
              case false => ('[', ']', getColor(gem.color))
            }
          }

          val targetX = b._2 + (x * 2)
          val targetY = b._3 + (b._1.height - y - 1)

          screen.setCharacter(targetX, targetY, new TextCharacter(pattern._1, pattern._3, bgColor))
          screen.setCharacter(targetX + 1, targetY, new TextCharacter(pattern._2, pattern._3, bgColor))
        }
      }
    }
    screen.refresh()
  }

  private[this] def getColor(c: Color) = c match {
    case Color.Red => TextColor.ANSI.RED
    case Color.Green => TextColor.ANSI.GREEN
    case Color.Blue => TextColor.ANSI.BLUE
    case Color.Yellow => TextColor.ANSI.YELLOW
    case Color.Wild => TextColor.ANSI.WHITE
  }
}
