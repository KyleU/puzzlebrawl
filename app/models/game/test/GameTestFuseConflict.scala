package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestFuseConflict extends GameTest.Provider {
  override def newInstance() = new GameTestFuseConflict()
}

class GameTestFuseConflict() extends GameTest(seed = 0) {
  override def init() = {
    board.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)
    board.add(Gem(1, Color.Red, width = Some(3), height = Some(3)), 2, 0)

    goal.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)
    goal.add(Gem(1, Color.Red, width = Some(3), height = Some(3)), 2, 0)
  }

  override def run() = board.fuse()
}
