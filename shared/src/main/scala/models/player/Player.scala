package models.player

import java.util.UUID
import models.board.Board
import models.gem.{ GemLocation, GemStream }

case class Player(
  id: UUID,
  name: String,
  team: Int,
  board: Board,
  gemStream: GemStream,
  script: Option[String] = None,
  var score: Int = 0,
  var activeGems: Seq[GemLocation] = Seq.empty,
  var target: Option[UUID] = None) extends ActiveGemHelper
