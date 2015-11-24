package models.game.player

import java.util.UUID
import models.game.board.Board
import models.game.gem.{ GemLocation, GemStream }

case class Player(
  id: UUID,
  name: String,
  board: Board,
  gemStream: GemStream,
  var score: Int = 0,
  var activeGems: Seq[GemLocation] = Seq.empty
) extends ActiveGemHelper
