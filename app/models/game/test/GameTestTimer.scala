package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestTimer extends GameTest.Provider {
  override def newInstance() = new GameTestTimer()
}

class GameTestTimer() extends GameTest(seed = 0) {
  override def init() = {
    board.add(Gem(0, Color.Red, timer = Some(5)), 0, 0)
    board.add(Gem(0, Color.Red, timer = Some(4)), 1, 0)
    board.add(Gem(0, Color.Red, timer = Some(3)), 2, 0)
    board.add(Gem(0, Color.Red, timer = Some(2)), 3, 0)
    board.add(Gem(0, Color.Red, timer = Some(1)), 4, 0)
    board.add(Gem(0, Color.Red), 5, 0)

    goal.add(Gem(0, Color.Red, timer = Some(4)), 0, 0)
    goal.add(Gem(0, Color.Red, timer = Some(3)), 1, 0)
    goal.add(Gem(0, Color.Red, timer = Some(2)), 2, 0)
    goal.add(Gem(0, Color.Red, timer = Some(1)), 3, 0)
    goal.add(Gem(0, Color.Red), 4, 0)
    goal.add(Gem(0, Color.Red), 5, 0)
  }

  override def run() = board.decrementTimers()
}
