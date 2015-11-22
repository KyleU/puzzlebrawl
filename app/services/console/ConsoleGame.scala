package services.console

import com.googlecode.lanterna.input.{ KeyType, KeyStroke }
import models.game.Game

import scala.util.Random

class ConsoleGame() extends ConsoleInput {
  val numPlayers = 4
  val game = Game.blank(playerNames = (0 until numPlayers).map(x => "Player " + (x + 1)))

  val client = new ConsoleClient(game)

  game.players.foreach { player =>
    (0 until 20).foreach { i =>
      player.board.drop(player.gemStream.next, Random.nextInt(player.board.width))
    }
    player.board.fullTurn()
    player.createActiveGems()
  }

  client.addStatusLog("Game started. Use the arrows keys to move and rotate, space to drop, and escape to quit.")

  private val playerOne = game.players.headOption.getOrElse(throw new IllegalStateException())

  client.setActivePlayer(playerOne.id)
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
    case x if x.getKeyType == KeyType.ArrowLeft => activeGemLeft(); true
    case x if x.getKeyType == KeyType.ArrowRight => activeGemRight(); true
    case x if x.getKeyType == KeyType.Character =>
      x.getCharacter match {
        case char if char.charValue == 'c' => game.players.foreach(_.board.collapse())
        case char if char.charValue == 'f' => game.players.foreach(_.board.fuse())
        case char if char.charValue == 'a' => activeGemLeft()
        case char if char.charValue == 'd' => activeGemRight()
        case char if char.charValue == ' ' =>
          val p = client.getActivePlayer
          p.dropActiveGems()
          p.board.fullTurn()
          p.createActiveGems()

        case char if char.charValue == '1' => client.setActivePlayer(game.players.headOption.getOrElse(throw new IllegalStateException()).id)
        case char if char.charValue == '2' => client.setActivePlayer(game.players(1).id)
        case char if char.charValue == '3' => client.setActivePlayer(game.players(2).id)
        case char if char.charValue == '4' => client.setActivePlayer(game.players(3).id)
        case char => client.addStatusLog(s"Unknown input: [$char].")
      }
      client.render()
      true
    case _ => true
  }

  private[this] def activeGemLeft() = {
    val p = client.getActivePlayer
    if(p.activeGems.map(_.x).min > 0) {
      p.activeGems = p.activeGems.map(loc => loc.copy(x = loc.x - 1))
      client.render()
    }
  }

  private[this] def activeGemRight() = {
    val p = client.getActivePlayer
    if(p.activeGems.map(_.x).max < p.board.width - 1) {
      p.activeGems = p.activeGems.map(loc => loc.copy(x = loc.x + 1))
      client.render()
    }
  }
}
