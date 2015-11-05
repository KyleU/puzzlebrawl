package models.test

import models.game.{Gem, Board}
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

    "drop single blocks" in {
      val a = Board(4, 4)
      a.add(Gem(0, Gem.Red), 0)
      a.add(Gem(1, Gem.Red, crash = true), 0)
      a.add(Gem(2, Gem.Green), 1)
      a.add(Gem(3, Gem.Green, crash = true), 1)
      a.add(Gem(4, Gem.Blue), 2)
      a.add(Gem(5, Gem.Blue, crash = true), 2)
      a.add(Gem(6, Gem.Yellow), 3)
      a.add(Gem(7, Gem.Yellow, crash = true), 3)

      a.toString ==="" +
        "....\n" +
        "....\n" +
        "RGBY\n" +
        "rgby"
    }
  }
}
