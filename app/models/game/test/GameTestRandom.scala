package models.game.test

import models.game.board.mutation.Mutation.AddGem

object GameTestRandom extends GameTest.Provider {
  override def newInstance() = new GameTestRandom()
}

class GameTestRandom() extends GameTest() {
  override def init() = {
    for (y <- 0 until goal.board.height) {
      for (x <- 0 until goal.board.width) {
        goal.board.applyMutation(AddGem(goal.gemStream.next, x, y))
      }
    }
  }

  override def run() = Seq(
    (0 until test.board.height).flatMap { y =>
      (0 until test.board.width).map { x =>
        test.board.applyMutation(AddGem(test.gemStream.next, x, y))
      }
    }
  )
}
