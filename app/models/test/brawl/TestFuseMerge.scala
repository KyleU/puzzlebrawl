package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.AddGem
import models.gem.Gem

object TestFuseMerge extends BrawlTest.Provider {
  override def newInstance(id: UUID, self: UUID) = new TestFuseMerge(id, self)
}

class TestFuseMerge(id: UUID, self: UUID) extends BrawlTest(id, self) {
  override def init() = {
    // Group 1
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))

    // Group 2
    test.board.applyMutation(AddGem(Gem(1, width = Some(2), height = Some(2)), 2, 0))

    // Additions
    test.board.applyMutation(AddGem(Gem(2), 4, 0))
    test.board.applyMutation(AddGem(Gem(3), 4, 1))

    // Lonely Single
    test.board.applyMutation(AddGem(Gem(4), 5, 0))

    // Goal
    goal.board.applyMutation(AddGem(Gem(0, width = Some(5), height = Some(2)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(4), 5, 0))
  }

  override def run() = test.board.fuse()
}
