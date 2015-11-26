package models.test.brawl

import models.board.mutation.Mutation.AddGem
import models.gem.{ Color, Gem }

object TestCombo extends Test.Provider {
  override def newInstance() = new TestCombo()
}

class TestCombo() extends Test() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0), 0, 0))
    test.board.applyMutation(AddGem(Gem(1), 1, 0))
    test.board.applyMutation(AddGem(Gem(2, crash = Some(true)), 0, 1))
    test.board.applyMutation(AddGem(Gem(3, Color.Green), 1, 1))
    test.board.applyMutation(AddGem(Gem(4, Color.Green, crash = Some(true)), 0, 2))
    test.board.applyMutation(AddGem(Gem(5, Color.Blue), 1, 2))
    test.board.applyMutation(AddGem(Gem(6, Color.Blue, crash = Some(true)), 2, 0))
    test.board.applyMutation(AddGem(Gem(7, Color.Yellow), 2, 1))
    test.board.applyMutation(AddGem(Gem(8, Color.Yellow, crash = Some(true)), 3, 0))
  }

  override def run() = {
    test.board.fullTurn()
  }
}
