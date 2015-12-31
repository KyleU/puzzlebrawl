package models.player

import java.util.UUID
import models.board.Board
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
    var target: Option[UUID] = None) extends ActiveGemHelper {

  final def dropActiveFullTurn = {
    val dropSegment = activeGemsDrop()
    val wildSegment = board.processWilds()
    val combo = if (wildSegment.isEmpty) { 1 } else { 2 }
    val postWildSegment = if (wildSegment.isEmpty) { Seq.empty } else { board.collapse() ++ board.fuse() }
    val fullTurn = board.fullTurn(combo = combo)

    val messages = (dropSegment +: wildSegment) ++ postWildSegment ++ fullTurn :+ activeGemsCreate()

    val scoreDelta = messages.flatMap(_.scoreDelta).sum
    score += scoreDelta

    val chargeDeltas = messages.flatMap(_.charge)
    println("chargeDeltas: " + chargeDeltas.mkString(", "))

    messages
  }
}
