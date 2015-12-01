package models.test.brawl

import models.board.mutation.Mutation.AddGem
import models.board.mutation.UpdateSegment

object TestClear extends Test.Provider {
  override def newInstance() = new TestClear()
}

class TestClear() extends Test() {
  override def init() = {
    for (y <- 0 until test.board.height / 2) {
      for (x <- 0 until test.board.width) {
        test.board.applyMutation(AddGem(test.gemStream.next, x, y))
      }
    }
  }

  override def run() = test.board.clear().map(x => UpdateSegment("clear", x))
}
