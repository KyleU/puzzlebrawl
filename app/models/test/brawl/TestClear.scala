package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.AddGem
import models.board.mutation.UpdateSegment

object TestClear extends BrawlTest.Provider {
  override def newInstance(id: UUID) = new TestClear(id)
}

class TestClear(id: UUID) extends BrawlTest(id) {
  override def init() = {
    for (y <- 0 until test.board.height / 2) {
      for (x <- 0 until test.board.width) {
        test.board.applyMutation(AddGem(test.gemStream.next, x, y))
      }
    }
  }

  override def run() = test.board.clear().map(x => UpdateSegment("clear", x))
}
