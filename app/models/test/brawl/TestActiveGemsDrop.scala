package models.test.brawl

import models.board.mutation.Mutation.AddGem

object TestActiveGemsDrop extends Test.Provider {
  override def newInstance() = new TestActiveGemsDrop()
}

class TestActiveGemsDrop() extends Test() {
  override def init() = {
    test.activeGemsCreate()

    goal.board.applyMutation(AddGem(goal.gemStream.next, 2, 0))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 3, 0))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 2, 1))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 3, 1))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 3, 2))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 4, 0))
  }

  override def run() = {
    val a = test.activeGemsDrop()
    test.activeGemsCreate()
    val b = test.activeGemsDrop()
    test.activeGemsCreate()
    test.activeGemsRight()
    val c = test.activeGemsDrop()
    Seq(a, b, c)
  }
}
