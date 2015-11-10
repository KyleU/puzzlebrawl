package services.console

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyStroke
import models.game.Board

object ConsoleTestA extends ConsoleTest {
  override def init() = {
    val rows = Math.floor(client.screen.getTerminalSize.getRows / 15.toDouble).toInt
    val cols = Math.floor(client.screen.getTerminalSize.getColumns / 15.toDouble).toInt

    (0 until rows).flatMap { boardY =>
      (0 until cols).map { boardX =>
        val board = Board(6, 12)
        ConsoleBorders.render(client, 1 + (15 * boardX), 1 + (15 * boardY), board.width, board.height, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK)

        (0 until 20).foreach { i =>
          board.drop(client.gemStream.next, r.nextInt(board.width))
        }

        client.add(board, 2 + (15 * boardX), 2 + (15 * boardY))
        board
      }
    }
  }

  override def inputCharacter(input: KeyStroke): Boolean = input.getCharacter match {
    case x if x == ' ' =>
      client.boards.foreach { b =>
        b._1.drop(client.gemStream.next, r.nextInt(b._1.width))
      }
      client.render()
      true
    case _ => false
  }
}
