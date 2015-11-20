package models.game.test

import models.game.gem.GemStream

object GameTestClear extends GameTest.Provider {
  override def newInstance() = GameTestClear()
}

case class GameTestClear() extends GameTest(seed = 0) {
  override def init() = {
    val boardStream = GemStream(seed)
    for(y <- 0 until board.height / 2) {
      for(x <- 0 until board.width) {
        board.add(boardStream.next, x, y)
      }
    }
  }

  override def run() = board.clear()
}
