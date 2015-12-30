package services.console

import java.util.UUID

import models.board.mutation.Mutation.AddGem
import models.brawl.Brawl

import scala.util.Random

class ConsoleGame() {
  val numPlayers = 4
  val game = Brawl.blank(
    id = UUID.randomUUID,
    players = (0 until numPlayers).map(x => (UUID.randomUUID, "Player " + (x + 1))),
    scenario = "Console"
  )
  val client = new ConsoleClient(game)

  val input = new ConsoleGameInput(game, client)

  game.players.foreach { player =>
    (0 until 20).foreach { i =>
      val x = Random.nextInt(player.board.width)
      player.board.applyMutation(AddGem(player.gemStream.next(), x, player.board.height - 1))
      player.board.drop(x, player.board.height - 1)
    }
    player.board.fullTurn(player)
    player.activeGemsCreate()
  }

  client.addStatusLog("Brawl started. Use the arrows keys to move and rotate, space to drop, and escape to quit.")
  client.setActivePlayer(game.players.headOption.getOrElse(throw new IllegalStateException()).id)
  client.render()
  input.startInputLoop(client)
  client.stop()
}
