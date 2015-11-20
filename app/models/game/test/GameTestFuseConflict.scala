package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestFuseConflict extends GameTest.Provider {
  override def newInstance() = new GameTestFuseConflict()
}

class GameTestFuseConflict() extends GameTest() {
  override def init() = {
    test.board.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)
    test.board.add(Gem(1, Color.Red, width = Some(3), height = Some(3)), 2, 0)

    goal.board.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)
    goal.board.add(Gem(1, Color.Red, width = Some(3), height = Some(3)), 2, 0)
  }

  override def run() = test.board.fuse()
}
