package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestCrash extends GameTest.Provider {
  override def newInstance() = new GameTestCrash()
}

class GameTestCrash() extends GameTest() {
  override def init() = {
    test.board.add(Gem(0, Color.Red), 0, 0)
    test.board.add(Gem(1, Color.Red, crash = true), 1, 0)
    test.board.add(Gem(2, Color.Red), 2, 0)
    test.board.add(Gem(3, Color.Red), 3, 0)
    test.board.add(Gem(4, Color.Red), 3, 1)
    test.board.add(Gem(5, Color.Red, width = Some(2), height = Some(2)), 4, 0)
    test.board.add(Gem(6, Color.Blue), 0, 1)
    test.board.add(Gem(7, Color.Green), 3, 2)

    goal.board.add(Gem(6, Color.Blue), 0, 0)
    goal.board.add(Gem(7, Color.Green), 3, 0)
  }

  override def run() = {
    test.board.crash()
    test.board.collapse()
  }
}
