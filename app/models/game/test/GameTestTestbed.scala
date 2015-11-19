package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestTestbed extends GameTest.Provider {
  override def newInstance() = GameTestTestbed()
}

case class GameTestTestbed() extends GameTest(seed = 0) {
  override def init() = {
    board.add(Gem(0, Color.Red, width = Some(2), height= Some(2)), 0, 0)

    board.add(Gem(1, Color.Red, width = Some(2), height= Some(2)), 0, 2)

    goal.add(Gem(0, Color.Red, width = Some(2), height = Some(4)), 0, 0)
  }

  override def run() = board.fuse()
}


