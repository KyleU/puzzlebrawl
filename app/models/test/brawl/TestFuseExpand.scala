package models.test.brawl

import models.board.mutation.Mutation.AddGem
import models.gem.Gem

object TestFuseExpand extends Test.Provider {
  override def newInstance() = new TestFuseExpand()
}

class TestFuseExpand() extends Test() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))
    test.board.applyMutation(AddGem(Gem(1), 2, 0))
    test.board.applyMutation(AddGem(Gem(2), 2, 1))
    test.board.applyMutation(AddGem(Gem(3), 0, 2))
    test.board.applyMutation(AddGem(Gem(4), 1, 2))
    test.board.applyMutation(AddGem(Gem(5), 2, 2))
    test.board.applyMutation(AddGem(Gem(6), 0, 3))
    test.board.applyMutation(AddGem(Gem(7), 3, 0))
    test.board.applyMutation(AddGem(Gem(8), 3, 1))
    test.board.applyMutation(AddGem(Gem(9, timer = Some(5)), 3, 2))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(3), height = Some(3)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(6), 0, 3))
    goal.board.applyMutation(AddGem(Gem(7), 3, 0))
    goal.board.applyMutation(AddGem(Gem(8), 3, 1))
    goal.board.applyMutation(AddGem(Gem(9, timer = Some(5)), 3, 2))
  }

  override def run() = test.board.fuse()
}
