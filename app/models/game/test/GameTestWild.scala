package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestWild extends GameTest.Provider {
  override def newInstance() = GameTestWild()
}

case class GameTestWild() extends GameTest(seed = 0) {
  override def init() = {
    board.add(Gem(0, Color.Red), 0, 0)
    board.add(Gem(1, Color.Red, width = Some(2), height = Some(2)), 1, 0)
    board.add(Gem(2, Color.Red, crash = true), 3, 0)
    board.add(Gem(3, Color.Red, timer = Some(1)), 4, 0)
    board.add(Gem(4, Color.Blue), 5, 0)
    board.add(Gem(5, Color.Wild), 1, 2)

    goal.add(Gem(2, Color.Red, crash = true), 3, 0)
    goal.add(Gem(3, Color.Red, timer = Some(1)), 4, 0)
    goal.add(Gem(4, Color.Blue), 5, 0)
  }

  override def run() = board.processWilds()
}
