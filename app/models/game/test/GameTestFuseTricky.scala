package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestFuseTricky extends GameTest.Provider {
  override def newInstance() = new GameTestFuseTricky()
}

class GameTestFuseTricky() extends GameTest() {
  override def init() = {
    test.board.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)
    test.board.add(Gem(1, Color.Red), 0, 2)
    test.board.add(Gem(2, Color.Red), 1, 2)

    test.board.add(Gem(3, Color.Red), 2, 0)
    test.board.add(Gem(4, Color.Red), 3, 0)
    test.board.add(Gem(5, Color.Red, width = Some(2), height = Some(2)), 2, 1)

    test.board.add(Gem(6, Color.Red, width = Some(2), height = Some(2)), 4, 0)
    test.board.add(Gem(7, Color.Red), 4, 2)

    goal.board.add(Gem(0, Color.Red, width = Some(4), height = Some(3)), 0, 0)
    goal.board.add(Gem(6, Color.Red, width = Some(2), height = Some(2)), 4, 0)
    goal.board.add(Gem(7, Color.Red), 4, 2)
  }

  override def run() = test.board.fuse()
}
