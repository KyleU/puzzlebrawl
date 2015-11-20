package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestFuseTrickyTwo extends GameTest.Provider {
  override def newInstance() = new GameTestFuseTrickyTwo()
}

class GameTestFuseTrickyTwo() extends GameTest() {
  override def init() = {
    test.board.add(Gem(0, Color.Red, width = Some(4), height = Some(2)), 0, 0)

    test.board.add(Gem(1, Color.Red), 2, 2)
    test.board.add(Gem(2, Color.Red), 3, 2)
    test.board.add(Gem(3, Color.Red, width = Some(2), height = Some(2)), 2, 3)

    test.board.add(Gem(4, Color.Red, width = Some(2), height = Some(2)), 0, 2)
    test.board.add(Gem(5, Color.Red), 0, 4)
    test.board.add(Gem(6, Color.Red), 1, 4)

    goal.board.add(Gem(0, Color.Red, width = Some(4), height = Some(5)), 0, 0)
  }

  override def run() = test.board.fuse()
}
