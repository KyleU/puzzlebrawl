package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestFuseExpand extends GameTest.Provider {
  override def newInstance() = new GameTestFuseExpand()
}

class GameTestFuseExpand() extends GameTest(seed = 0) {
  override def init() = {
    board.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)
    board.add(Gem(1, Color.Red), 2, 0)
    board.add(Gem(2, Color.Red), 2, 1)
    board.add(Gem(3, Color.Red), 0, 2)
    board.add(Gem(4, Color.Red), 1, 2)
    board.add(Gem(5, Color.Red), 2, 2)
    board.add(Gem(6, Color.Red), 0, 3)
    board.add(Gem(7, Color.Red), 3, 0)
    board.add(Gem(8, Color.Red), 3, 1)
    board.add(Gem(9, Color.Red, timer = Some(5)), 3, 2)

    goal.add(Gem(0, Color.Red, width = Some(3), height = Some(3)), 0, 0)
    goal.add(Gem(6, Color.Red), 0, 3)
    goal.add(Gem(7, Color.Red), 3, 0)
    goal.add(Gem(8, Color.Red), 3, 1)
    goal.add(Gem(9, Color.Red, timer = Some(5)), 3, 2)
  }

  override def run() = board.fuse()
}
