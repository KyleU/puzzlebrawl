package models.game.test

import models.game.board.mutation.Mutation.AddGem

object GameTestClear extends GameTest.Provider {
  override def newInstance() = new GameTestClear()
}

class GameTestClear() extends GameTest() {
  override def init() = {
    for (y <- 0 until test.board.height / 2) {
      for (x <- 0 until test.board.width) {
        test.board.applyMutation(AddGem(test.gemStream.next, x, y))
      }
    }
  }

  override def run() = test.board.clear()
}
