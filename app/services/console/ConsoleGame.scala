package services.console

import models.game.Game
import scala.util.Random

class ConsoleGame() {
  val numPlayers = 4
  val game = Game.blank(playerNames = (0 until numPlayers).map(x => "Player " + (x + 1)))
  val client = new ConsoleClient(game)

  val input = new ConsoleGameInput(game, client)

  game.players.foreach { player =>
    (0 until 20).foreach { i =>
      player.board.drop(player.gemStream.next, Random.nextInt(player.board.width))
    }
    player.board.fullTurn()
    player.createActiveGems()
  }

  client.addStatusLog("Game started. Use the arrows keys to move and rotate, space to drop, and escape to quit.")
  client.setActivePlayer(game.players.headOption.getOrElse(throw new IllegalStateException()).id)
  client.render()
  input.startInputLoop(client)
  client.stop()
}
