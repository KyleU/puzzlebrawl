package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestDrop extends GameTest.Provider {
  override def newInstance() = new GameTestDrop()
}

class GameTestDrop() extends GameTest(seed = 0) {
  override def init() = {
    goal.add(Gem(0, Color.Red), 0, 0)
    goal.add(Gem(1, Color.Green), 0, 1)
    goal.add(Gem(2, Color.Blue), 0, 2)
    goal.add(Gem(3, Color.Yellow), 0, 3)
    goal.add(Gem(4, Color.Red, crash = true), 1, 0)
    goal.add(Gem(5, Color.Green, crash = true), 1, 1)
    goal.add(Gem(6, Color.Blue, crash = true), 1, 2)
    goal.add(Gem(7, Color.Yellow, crash = true), 1, 3)
    goal.add(Gem(8, Color.Wild), 2, 0)
    goal.add(Gem(9, Color.Red, width = Some(2), height = Some(2)), 4, 0)
  }

  override def run() = {
    board.drop(Gem(0, Color.Red), 0)
    board.drop(Gem(1, Color.Green), 0)
    board.drop(Gem(2, Color.Blue), 0)
    board.drop(Gem(3, Color.Yellow), 0)
    board.drop(Gem(4, Color.Red, crash = true), 1)
    board.drop(Gem(5, Color.Green, crash = true), 1)
    board.drop(Gem(6, Color.Blue, crash = true), 1)
    board.drop(Gem(7, Color.Yellow, crash = true), 1)
    board.drop(Gem(8, Color.Wild), 2)
    board.drop(Gem(9, Color.Red, width = Some(2), height = Some(2)), 4)
  }
}
