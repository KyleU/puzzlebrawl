package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestDrop extends GameTest.Provider {
  override def newInstance() = new GameTestDrop()
}

class GameTestDrop() extends GameTest() {
  override def init() = {
    goal.board.add(Gem(0, Color.Red), 0, 0)
    goal.board.add(Gem(1, Color.Green), 0, 1)
    goal.board.add(Gem(2, Color.Blue), 0, 2)
    goal.board.add(Gem(3, Color.Yellow), 0, 3)
    goal.board.add(Gem(4, Color.Red, crash = true), 1, 0)
    goal.board.add(Gem(5, Color.Green, crash = true), 1, 1)
    goal.board.add(Gem(6, Color.Blue, crash = true), 1, 2)
    goal.board.add(Gem(7, Color.Yellow, crash = true), 1, 3)
    goal.board.add(Gem(8, Color.Wild), 2, 0)
    goal.board.add(Gem(9, Color.Red, width = Some(2), height = Some(2)), 4, 0)
  }

  override def run() = {
    test.board.drop(Gem(0, Color.Red), 0)
    test.board.drop(Gem(1, Color.Green), 0)
    test.board.drop(Gem(2, Color.Blue), 0)
    test.board.drop(Gem(3, Color.Yellow), 0)
    test.board.drop(Gem(4, Color.Red, crash = true), 1)
    test.board.drop(Gem(5, Color.Green, crash = true), 1)
    test.board.drop(Gem(6, Color.Blue, crash = true), 1)
    test.board.drop(Gem(7, Color.Yellow, crash = true), 1)
    test.board.drop(Gem(8, Color.Wild), 2)
    test.board.drop(Gem(9, Color.Red, width = Some(2), height = Some(2)), 4)
  }
}
