package models.game

import java.util.UUID

import models.game.board.Board
import models.game.gem.{ Gem, GemStream }

case class Player(
  id: UUID,
  name: String,
  board: Board,
  gemStream: GemStream,
  var score: Int = 0,
  var activeGems: Seq[Gem] = Seq.empty
)
