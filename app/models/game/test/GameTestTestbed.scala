package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestTestbed extends GameTest.Provider {
  override def newInstance() = new GameTestTestbed()
}

class GameTestTestbed() extends GameTest() {
  override def init() = {
    test.board.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)
    test.board.add(Gem(1, Color.Red, width = Some(2), height = Some(2)), 0, 2)

    goal.board.add(Gem(0, Color.Red, width = Some(2), height = Some(4)), 0, 0)
  }

  override def run() = test.board.fuse()
}

