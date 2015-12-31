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
    val dropSegment = activeGemsDrop()
    val timerSegment = board.decrementTimers()
    val wildSegment = board.processWilds()
    val combo = if (wildSegment.isEmpty) { 1 } else { 2 }
    val postWildSegment = if (wildSegment.isEmpty) { Seq.empty } else { board.collapse() ++ board.fuse() }
    val fullTurn = board.fullTurn(combo = combo)

    val timerDump = if (pendingGems.isEmpty) {
      None
    } else {
      Some(UpdateSegment("pending", pendingGems.flatMap { pg =>
        this.board.applyMutation(AddGem(pg.gem, pg.x, board.height - 1)) +: this.board.drop(pg.x, board.height - 1)
      }))
    }

    val phaseOne = dropSegment +: timerSegment.toSeq
    val phaseTwo = wildSegment ++ postWildSegment
    val phaseThree = fullTurn :+ activeGemsCreate()
    val messages = phaseOne ++ phaseTwo ++ phaseThree ++ timerDump.toSeq

    val scoreDelta = messages.flatMap(_.scoreDelta).sum
    score += scoreDelta

    val chargeDeltas = messages.flatMap(_.charge).filter(_ != 0.0)
    if (chargeDeltas.nonEmpty) {
      brawl.applyCharge(id, chargeDeltas)
    }

    messages
  }
}
