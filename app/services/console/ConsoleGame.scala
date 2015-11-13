package services.console

import com.googlecode.lanterna.input.{ KeyType, KeyStroke }
import models.game.board.Board

import scala.util.Random

object ConsoleGame {
  def main(args: Array[String]) {
    new ConsoleGame()
  }
}

class ConsoleGame() extends ConsoleInput {
  val client = new ConsoleClient()
  val rows = Math.floor(client.rows / 15.toDouble).toInt
  val cols = Math.floor(client.cols / 15.toDouble).toInt

  val boards = (0 until (rows * cols)).map { i =>
    val board = Board(s"board-$i", 6, 12)

    (0 until 20).foreach { i =>
      board.drop(client.gemStream.next, Random.nextInt(board.width))
    }

    client.add(board)
    board
  }

  client.render()

  startInputLoop(client)

  override def inputCharacter(input: KeyStroke): Boolean = input match {
    case x if x.getKeyType == KeyType.Enter =>
      boards.foreach { b =>
        b.drop(client.gemStream.next, Random.nextInt(b.width))
      }
      client.render()
      true
    case _ => true
  }
}
