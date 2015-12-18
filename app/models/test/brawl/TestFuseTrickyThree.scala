package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.AddGem
import models.gem.Gem

object TestFuseTrickyThree extends BrawlTest.Provider {
  override def newInstance(id: UUID, self: UUID) = new TestFuseTrickyThree(id, self)
}

class TestFuseTrickyThree(id: UUID, self: UUID) extends BrawlTest(id, self) {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(4)), 0, 0))

    test.board.applyMutation(AddGem(Gem(1, width = Some(2), height = Some(2)), 2, 2))
    test.board.applyMutation(AddGem(Gem(2), 4, 2))
    test.board.applyMutation(AddGem(Gem(3), 4, 3))

    test.board.applyMutation(AddGem(Gem(4), 2, 0))
    test.board.applyMutation(AddGem(Gem(5, width = Some(2), height = Some(2)), 3, 0))
    test.board.applyMutation(AddGem(Gem(6), 2, 1))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(5), height = Some(4)), 0, 0))
  }

  override def run() = test.board.fuse()
}
