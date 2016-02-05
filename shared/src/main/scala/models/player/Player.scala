package models.player

import java.util.UUID

import models.board.Board
import models.gem.{ GemLocation, GemPattern, GemStream }

final case class Player(
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
    var target: Option[UUID] = None,
    var status: String = "active",
    var completed: Option[Long] = None) extends ActiveGemHelper with TurnHelper {

  def isActive = status == "active"
  def isComplete = completed.isDefined
}
