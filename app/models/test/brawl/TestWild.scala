package models.test.brawl

import models.board.mutation.Mutation.AddGem
import models.gem.{ Color, Gem }

object TestWild extends Test.Provider {
  override def newInstance() = new TestWild()
}

class TestWild() extends Test() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0), 0, 0))
    test.board.applyMutation(AddGem(Gem(1, width = Some(2), height = Some(2)), 1, 0))
    test.board.applyMutation(AddGem(Gem(2, crash = Some(true)), 3, 0))
    test.board.applyMutation(AddGem(Gem(3, timer = Some(1)), 4, 0))
    test.board.applyMutation(AddGem(Gem(4, Color.Blue), 5, 0))
    test.board.applyMutation(AddGem(Gem(5, Color.Wild), 1, 2))

    goal.board.applyMutation(AddGem(Gem(2, crash = Some(true)), 3, 0))
    goal.board.applyMutation(AddGem(Gem(3, timer = Some(1)), 4, 0))
    goal.board.applyMutation(AddGem(Gem(4, Color.Blue), 5, 0))
  }

  override def run() = test.board.processWilds()
}
