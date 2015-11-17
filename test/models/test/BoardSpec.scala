package models.test

import models.game.board.Board
import models.game.gem.{ Color, Gem }
import org.specs2.mutable._

class BoardSpec extends Specification {
  "The game board" should {
    "compare correctly" in {
      val a = Board("a", 10, 10)
      val b = Board("b", 10, 10)
      val c = Board("c", 1, 1)
      a === b
      b !== c
    }

    "insert single blocks" in {
      val b = Board("board", 4, 4)
      b.add(Gem(0, Color.Red), 0, 0)
      b.add(Gem(1, Color.Red, crash = true), 0, 1)

      b.at(0, 0) === Some(Gem(0, Color.Red))
      b.at(0, 1) === Some(Gem(1, Color.Red, crash = true))
    }
  }
}
