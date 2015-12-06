package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.AddGem
import models.board.mutation.UpdateSegment

object TestDropFull extends BrawlTest.Provider {
  override def newInstance(id: UUID) = new TestDropFull(id)
}

class TestDropFull(id: UUID) extends BrawlTest(id) {
  override def init() = {
    (0 until goal.board.height).foreach(i => goal.board.applyMutation(AddGem(goal.gemStream.next, 0, i)))
  }

  override def run() = {
    Seq(UpdateSegment("drop", (0 until test.board.height).flatMap { i =>
      test.board.applyMutation(AddGem(test.gemStream.next, 0, test.board.height - 1)) +: test.board.drop(0, test.board.height - 1)
    }))
  }
}
