package models.game

import java.util.{ UUID, Date }

import models.game.board.Board
import models.game.gem.GemStream
import models.game.player.Player

import scala.util.Random

object Game {
  def blank(
    id: UUID = UUID.randomUUID,
    seed: Int = Math.abs(Random.nextInt()),
    playerNames: Seq[String] = Seq("Player 1"),
    width: Int = 6,
    height: Int = 12
  ) = {
    val players = playerNames.map(name => Player(UUID.randomUUID, name, Board(name, width, height), gemStream = GemStream(seed)))
    Game(id, seed, players)
  }

  def random(playerNames: Seq[String] = Seq("Player 1"), width: Int = 6, height: Int = 12, initialDrops: Int = 0) = {
    val game = blank(playerNames = playerNames, width = width, height = height)
    (0 until initialDrops).foreach(_ => game.players.foreach { p =>
      p.board.drop(p.gemStream.next, Random.nextInt(width))
    })
    game
  }
}

case class Game(id: UUID, seed: Int, players: Seq[Player], started: Long = new Date().getTime) {
  private[this] val rng = new Random(seed)

  val playersById = players.map(p => p.id -> p).toMap
  if (playersById.size != players.size) {
    throw new IllegalStateException(s"Game cannot have duplicate players: [${players.map(_.name).mkString(", ")}]")
  }
}
