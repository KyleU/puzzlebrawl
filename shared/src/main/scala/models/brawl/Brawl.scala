package models.brawl

import java.util.{ Date, UUID }

import models.board.Board
import models.gem.GemStream
import models.player.Player

import scala.util.Random

object Brawl {
  trait Callbacks {
    def onLoss(playerId: UUID)
    def onComplete()
  }

  def blank(id: UUID, scenario: String, seed: Int = Math.abs(Random.nextInt()), players: Seq[(UUID, String)] = Seq(UUID.randomUUID -> "Player 1")) = {
    Brawl(id, scenario, seed, players.zipWithIndex.map(x => Player(x._1._1, x._1._2, x._2, Board(x._1._2, 6, 12), gemStream = GemStream(seed))))
  }
}

case class Brawl(
    id: UUID,
    scenario: String,
    seed: Int,
    players: Seq[Player],
    var status: String = "active",
    started: Long = new Date().getTime,
    var completed: Option[Long] = None) extends ChargeHelper with CompletionHelper {

  protected[this] val rng = new Random(seed)

  val playersById = players.map(p => p.id -> p).toMap
  setInitialTargets()

  protected[this] var callbacks: Option[Brawl.Callbacks] = None
  def setCallbacks(c: Brawl.Callbacks) = callbacks = Some(c)

  def elapsedMs = (completed.getOrElse(new Date().getTime) - started).toInt

  def isActive = status == "active"
  def isComplete = status == "complete"

  private[this] def setInitialTargets() = {
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
}
