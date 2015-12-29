package models.brawl

import java.util.{ Date, UUID }

import models.board.Board
import models.board.mutation.Mutation.AddGem
import models.gem.GemStream
import models.player.Player

import scala.util.Random

object Brawl {
  def blank(
    id: UUID,
    scenario: String,
    seed: Int = Math.abs(Random.nextInt()),
    players: Seq[(UUID, String)] = Seq(UUID.randomUUID -> "Player 1"),
    width: Int = 6,
    height: Int = 12) = {
    val ps = players.map(x => Player(x._1, x._2, Board(x._2, width, height), gemStream = GemStream(seed)))
    Brawl(id, scenario, seed, ps)
  }

  def random(scenario: String, players: Seq[(UUID, String)] = Seq(UUID.randomUUID -> "Player 1"), width: Int = 6, height: Int = 12, initialDrops: Int = 0) = {
    val game = blank(UUID.randomUUID, players = players, scenario = scenario, width = width, height = height)
    (0 until initialDrops).foreach(_ => game.players.foreach { p =>
      val x = Random.nextInt(p.board.width)
      p.board.applyMutation(AddGem(p.gemStream.next, x, p.board.height - 1))
      p.board.drop(x, p.board.height - 1)
    })
    game
  }
}

case class Brawl(
    id: UUID,
    scenario: String,
    seed: Int,
    players: Seq[Player],
    started: Long = new Date().getTime,
    var completed: Option[Long] = None) {
  private[this] val rng = new Random(seed)

  val playersById = players.map(p => p.id -> p).toMap
  if (playersById.size != players.size) {
    throw new IllegalStateException(s"Brawl cannot have duplicate players: [${players.map(_.name).mkString(", ")}]")
  }

  if (players.size > 1) {
    players.zipWithIndex.foreach { player =>
      if (player._1.target.isEmpty) {
        player._1.target = Some(players((player._2 + 1) % players.length).id)
      }
    }
  }
}
