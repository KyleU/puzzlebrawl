package models.game.test

import models.game.board.mutation.Mutation.AddGem

object GameTestActiveGems extends GameTest.Provider {
  override def newInstance() = new GameTestActiveGems()
}

class GameTestActiveGems() extends GameTest() {
  override def init() = {
    test.createActiveGems()

    goal.board.applyMutation(AddGem(goal.gemStream.next, 2, 0))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 3, 0))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 2, 1))
    goal.board.applyMutation(AddGem(goal.gemStream.next, 3, 1))
  }

  override def run() = {
    val a = test.dropActiveGems()
    test.createActiveGems()
    val b = test.dropActiveGems()
    Seq(a, b)
  }
}
