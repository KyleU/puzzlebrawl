package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestFuseTricky extends GameTest.Provider {
  override def newInstance() = new GameTestFuseTricky()
}

class GameTestFuseTricky() extends GameTest(seed = 0) {
  override def init() = {
    board.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)
    board.add(Gem(1, Color.Red), 0, 2)
    board.add(Gem(2, Color.Red), 1, 2)

    board.add(Gem(3, Color.Red), 2, 0)
    board.add(Gem(4, Color.Red), 3, 0)
    board.add(Gem(5, Color.Red, width = Some(2), height = Some(2)), 2, 1)

    board.add(Gem(6, Color.Red, width = Some(2), height = Some(2)), 4, 0)
    board.add(Gem(7, Color.Red), 4, 2)

    goal.add(Gem(0, Color.Red, width = Some(4), height = Some(3)), 0, 0)
    goal.add(Gem(6, Color.Red, width = Some(2), height = Some(2)), 4, 0)
    goal.add(Gem(7, Color.Red), 4, 2)
  }

  override def run() = board.fuse()
}
