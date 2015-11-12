package models.test

import models.game.board.Board
import models.game.Color
import models.game.gem.Gem
import org.specs2.mutable._

class BoardSpec extends Specification {
  "The game board" should {
    "compare correctly" in {
      val a = Board(10, 10)
      val b = Board(10, 10)
      val c = Board(1, 1)
      a === b
      b !== c
    }

    "insert single blocks" in {
      val b = Board(4, 4)
      b.add(Gem(0, Color.Red), 0, 0)
      b.add(Gem(1, Color.Red, crash = true), 0, 1)

      b.spaces(0)(0) === Some(Gem(0, Color.Red))
      b.spaces(0)(1) === Some(Gem(1, Color.Red, crash = true))
    }
  }
}
