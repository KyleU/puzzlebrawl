package services.console

import com.googlecode.lanterna.input.{ KeyType, KeyStroke }
import models.game.Board

object ConsoleTestBasic extends ConsoleTest {
  override def init() = {
    val rows = Math.floor(client.rows / 15.toDouble).toInt
    val cols = Math.floor(client.cols / 15.toDouble).toInt

    (0 until (rows * cols)).map { i =>
      val board = Board(6, 12)

      (0 until 20).foreach { i =>
        board.drop(client.gemStream.next, r.nextInt(board.width))
      }

      client.add(board)
      board
    }
  }

  override def inputCharacter(input: KeyStroke): Boolean = input match {
    case x if x.getKeyType == KeyType.Enter =>
      client.boards.foreach { b =>
        b._1.drop(client.gemStream.next, r.nextInt(b._1.width))
      }
      client.render()
      true
    case _ => true
  }
}
