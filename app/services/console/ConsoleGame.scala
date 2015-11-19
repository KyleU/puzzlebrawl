package services.console

import com.googlecode.lanterna.input.{ KeyType, KeyStroke }
import models.game.Game

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

  val numPlayers = rows * cols
  val game = Game.blank(playerNames = (0 until numPlayers).map(x => "board-" + x))

  game.players.foreach { player =>
    (0 until 20).foreach { i =>
      player.board.drop(client.gemStream.next, Random.nextInt(player.board.width))
    }
    client.add(player.board)
  }

  client.addStatusLog("Game started. Use the arrows keys to move and rotate, space to drop, and escape to quit.")

  client.render()

  startInputLoop(client)

  override def inputCharacter(input: KeyStroke): Boolean = input match {
    case x if x.getKeyType == KeyType.Enter =>
      game.players.foreach { player =>
        player.board.drop(client.gemStream.next, Random.nextInt(player.board.width))
      }
      client.render()
      true
    case x if x.getKeyType == KeyType.ArrowLeft => activeGemLeft(); true
    case x if x.getKeyType == KeyType.ArrowRight => activeGemRight(); true
    case x if x.getKeyType == KeyType.Character =>
      x.getCharacter match {
        case char if char == 'c' => game.players.foreach(_.board.collapse())
        case char if char == 'f' => game.players.foreach(_.board.fuse())
        case char if char == 'a' => activeGemLeft()
        case char if char == 'd' => activeGemRight()
        case char => client.addStatusLog(s"Unknown input: [$char].")
      }
      client.render()
      true
    case _ => true
  }

  private[this] def activeGemLeft() = {

  }

  private[this] def activeGemRight() = {

  }
}
