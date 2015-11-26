package models.test.brawl

import models.board.mutation.Mutation.AddGem
import models.gem.Gem

object TestFuseTrickyTwo extends Test.Provider {
  override def newInstance() = new TestFuseTrickyTwo()
}

class TestFuseTrickyTwo() extends Test() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(4), height = Some(2)), 0, 0))

    test.board.applyMutation(AddGem(Gem(1), 2, 2))
    test.board.applyMutation(AddGem(Gem(2), 3, 2))
    test.board.applyMutation(AddGem(Gem(3, width = Some(2), height = Some(2)), 2, 3))

    test.board.applyMutation(AddGem(Gem(4, width = Some(2), height = Some(2)), 0, 2))
    test.board.applyMutation(AddGem(Gem(5), 0, 4))
    test.board.applyMutation(AddGem(Gem(6), 1, 4))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(4), height = Some(5)), 0, 0))
  }

  override def run() = test.board.fuse()
}
