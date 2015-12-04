package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.AddGem
import models.gem.Gem

object TestFuseConflict extends Test.Provider {
  override def newInstance(id: UUID) = new TestFuseConflict(id)
}

class TestFuseConflict(id: UUID) extends Test(id) {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))
    test.board.applyMutation(AddGem(Gem(1, width = Some(3), height = Some(3)), 2, 0))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(1, width = Some(3), height = Some(3)), 2, 0))
  }

  override def run() = test.board.fuse()
}
