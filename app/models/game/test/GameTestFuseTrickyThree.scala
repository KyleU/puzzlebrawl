package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestFuseTrickyThree extends GameTest.Provider {
  override def newInstance() = new GameTestFuseTrickyThree()
}

class GameTestFuseTrickyThree() extends GameTest(seed = 0) {
  override def init() = {
    board.add(Gem(0, Color.Red, width = Some(2), height = Some(4)), 0, 0)

    board.add(Gem(1, Color.Red, width = Some(2), height = Some(2)), 2, 2)
    board.add(Gem(2, Color.Red), 4, 2)
    board.add(Gem(3, Color.Red), 4, 3)

    board.add(Gem(4, Color.Red), 2, 0)
    board.add(Gem(5, Color.Red, width = Some(2), height = Some(2)), 3, 0)
    board.add(Gem(6, Color.Red), 2, 1)

    goal.add(Gem(0, Color.Red, width = Some(5), height = Some(4)), 0, 0)
  }

  override def run() = board.fuse()
}
