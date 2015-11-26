package models.player

import java.util.UUID
import models.board.Board
import models.gem.{ GemLocation, GemStream }

case class Player(
  id: UUID,
  name: String,
  board: Board,
  gemStream: GemStream,
  var score: Int = 0,
  var activeGems: Seq[GemLocation] = Seq.empty
) extends ActiveGemHelper