package models.test.brawl

import models.board.mutation.Mutation.AddGem
import models.gem.Gem

object TestTestbed extends Test.Provider {
  override def newInstance() = new TestTestbed()
}

class TestTestbed() extends Test() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))
    test.board.applyMutation(AddGem(Gem(1, width = Some(2), height = Some(2)), 0, 2))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(4)), 0, 0))
  }

  override def run() = test.board.fuse()
}

