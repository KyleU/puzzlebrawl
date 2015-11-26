package models.test.brawl

import models.board.mutation.Mutation.AddGem

object TestDropFull extends Test.Provider {
  override def newInstance() = new TestDropFull()
}

class TestDropFull() extends Test() {
  override def init() = {
    (0 until goal.board.height).foreach(i => goal.board.applyMutation(AddGem(goal.gemStream.next, 0, i)))
  }

  override def run() = {
    (0 until test.board.height).map(i => test.board.drop(test.gemStream.next, 0))
  }
}
