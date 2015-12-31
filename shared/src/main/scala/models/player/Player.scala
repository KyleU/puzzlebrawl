package models.player

import java.util.UUID
import models.board.Board
import models.board.mutation.Mutation.AddGem
import models.board.mutation.UpdateSegment
import models.brawl.Brawl
import models.gem.{ GemPattern, GemLocation, GemStream }

case class Player(
    id: UUID,
    name: String,
    team: Int,
    board: Board,
    gemStream: GemStream,
    gemPattern: String = GemPattern.default.key,
    script: Option[String] = None,
    var score: Int = 0,
    var activeGems: Seq[GemLocation] = Seq.empty,
    var pendingGems: Seq[GemLocation] = Seq.empty,
    var target: Option[UUID] = None) extends ActiveGemHelper {

  final def dropActiveFullTurn(brawl: Brawl) = {
    val dropSegment = Seq(activeGemsDrop())

    val timerSegment = board.decrementTimers().toSeq

    val wildSegment = board.processWilds()
    val combo = if (wildSegment.isEmpty) { 1 } else { 2 }
    val postWildSegment = if (wildSegment.isEmpty) { Seq.empty } else { board.collapse() ++ board.fuse() }

    val fullTurnSegments = board.fullTurn(combo = combo)

    val pendingSegment = processPendingGems()
    val postPendingSegment = if (pendingSegment.isEmpty) { Seq.empty } else { board.collapse() ++ board.fuse() }

    val activeCreate = Seq(activeGemsCreate())

    val messages = dropSegment ++ timerSegment ++ wildSegment ++ postWildSegment ++ fullTurnSegments ++ pendingSegment ++ postPendingSegment ++ activeCreate

    val scoreDelta = messages.flatMap(_.scoreDelta).sum
    score += scoreDelta

    val chargeDeltas = messages.flatMap(_.charge).filter(_ != 0.0)
    if (chargeDeltas.nonEmpty) {
      brawl.applyCharge(id, chargeDeltas)
    }

    messages
  }

  private[this] def processPendingGems() = if (pendingGems.isEmpty) {
    Seq.empty
  } else {
    val groupedGems = pendingGems.groupBy(_.x).map(x => x._1 -> x._2.map(_.gem).reverse.zipWithIndex)
    val additions = (0 until board.width).flatMap { i =>
      groupedGems.getOrElse(i, Seq.empty).map { gem =>
        this.board.applyMutation(AddGem(gem._1, i, board.height - gem._2 - 1))
      }
    }
    pendingGems = Seq.empty
    Seq(UpdateSegment("pending", additions))
  }
}
