package models.test.brawl

import models.board.mutation.Mutation.AddGem

object TestActiveGemsDrop extends Test.Provider {
  override def newInstance() = new TestActiveGemsDrop()
}

class TestActiveGemsDrop() extends Test() {
  override def init() = {
    test.createActiveGems()

    goal.board.applyMutation(AddGem(goal.gemStream.next, 2, 0))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 3, 0))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 2, 1))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 3, 1))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 3, 2))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 4, 0))
  }

  override def run() = {
    val a = test.dropActiveGems()
    test.createActiveGems()
    val b = test.dropActiveGems()
    test.createActiveGems()
    test.activeGemsRight()
    val c = test.dropActiveGems()
    Seq(a, b, c)
  }
}
