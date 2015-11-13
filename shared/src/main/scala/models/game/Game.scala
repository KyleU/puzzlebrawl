package models.game

import java.util.{ UUID, Date }

import models.game.board.Board
import models.game.gem.GemStream

import scala.util.Random

object Game {
  def blank(
    id: UUID = UUID.randomUUID,
    seed: Int = Math.abs(Random.nextInt()),
    playerNames: Seq[String] = Seq("Player 1"),
    width: Int = 6,
    height: Int = 12
  ) = {
    val players = playerNames.map( name => Player(name, Board(name, width, height), gemStream = GemStream(seed)))
    Game(id, seed, players)
  }
}

case class Game(id: UUID, seed: Int, players: Seq[Player]) {
  private[this] val rng = new Random(seed)
  val started = new Date().getTime
}
