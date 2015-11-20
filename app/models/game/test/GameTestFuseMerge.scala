package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestFuseMerge extends GameTest.Provider {
  override def newInstance() = new GameTestFuseMerge()
}

class GameTestFuseMerge() extends GameTest() {
  override def init() = {
    // Group 1
    test.board.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)

    // Group 2
    test.board.add(Gem(1, Color.Red, width = Some(2), height = Some(2)), 2, 0)

    // Additions
    test.board.add(Gem(2, Color.Red), 4, 0)
    test.board.add(Gem(3, Color.Red), 4, 1)

    // Lonely Single
    test.board.add(Gem(4, Color.Red), 5, 0)

    // Goal
    goal.board.add(Gem(0, Color.Red, width = Some(5), height = Some(2)), 0, 0)
    goal.board.add(Gem(4, Color.Red), 5, 0)
  }

  override def run() = test.board.fuse()
}
