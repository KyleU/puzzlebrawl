package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.AddGem
import models.gem.{ Color, Gem }

object TestCrash extends BrawlTest.Provider {
  override def newInstance(id: UUID, self: UUID) = new TestCrash(id, self)
}

class TestCrash(id: UUID, self: UUID) extends BrawlTest(id, self) {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0), 0, 0))
    test.board.applyMutation(AddGem(Gem(1, crash = Some(true)), 1, 0))
    test.board.applyMutation(AddGem(Gem(2), 2, 0))
    test.board.applyMutation(AddGem(Gem(3), 2, 1))
    test.board.applyMutation(AddGem(Gem(4), 3, 1))
    test.board.applyMutation(AddGem(Gem(5, width = Some(2), height = Some(2)), 4, 0))
    test.board.applyMutation(AddGem(Gem(6, Color.Blue), 0, 1))
    test.board.applyMutation(AddGem(Gem(7, Color.Green), 3, 2))
    test.board.applyMutation(AddGem(Gem(8), 5, 2))
    test.board.applyMutation(AddGem(Gem(9, Color.Blue), 3, 0))
    //test.board.applyMutation(AddGem(Gem(10), 5, 2))

    goal.board.applyMutation(AddGem(Gem(6, Color.Blue), 0, 0))
    goal.board.applyMutation(AddGem(Gem(7, Color.Green), 3, 1))
    goal.board.applyMutation(AddGem(Gem(9, Color.Blue), 3, 0))
  }

  override def run() = {
    test.board.crash(None) ++ test.board.collapse().toSeq
  }
}
