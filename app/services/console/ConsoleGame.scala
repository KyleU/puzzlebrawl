package services.console

import com.googlecode.lanterna.input.{ KeyType, KeyStroke }
import models.game.Game

import scala.util.Random

class ConsoleGame() extends ConsoleInput {
  val numPlayers = 4
  val game = Game.blank(playerNames = (0 until numPlayers).map(x => "Player " + x))

  val client = new ConsoleClient(game)

  game.players.foreach { player =>
    (0 until 20).foreach { i =>
      player.board.drop(player.gemStream.next, Random.nextInt(player.board.width))
    }
  }

  client.addStatusLog("Game started. Use the arrows keys to move and rotate, space to drop, and escape to quit.")

  client.render()

  startInputLoop(client)

  client.stop()

  override def inputCharacter(input: KeyStroke): Boolean = input match {
    case x if x.getKeyType == KeyType.Enter =>
      game.players.foreach { player =>
        player.board.drop(player.gemStream.next, Random.nextInt(player.board.width))
      }
      client.render()
      true
    case x if x.getKeyType == KeyType.ArrowLeft => activeGemLeft()
    case x if x.getKeyType == KeyType.ArrowRight => activeGemRight()
    case x if x.getKeyType == KeyType.Character =>
      x.getCharacter match {
        case char if char.charValue == 'c' => game.players.foreach(_.board.collapse())
        case char if char.charValue == 'f' => game.players.foreach(_.board.fuse())
        case char if char.charValue == 'a' => activeGemLeft()
        case char if char.charValue == 'd' => activeGemRight()
        case char => client.addStatusLog(s"Unknown input: [$char].")
      }
      client.render()
      true
    case _ => true
  }

  private[this] def activeGemLeft() = {
    true
  }

  private[this] def activeGemRight() = {
    true
  }
}
