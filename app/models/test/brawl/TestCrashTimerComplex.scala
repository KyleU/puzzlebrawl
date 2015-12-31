package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.AddGem
import models.gem.{ Color, Gem }

object TestCrashTimerComplex extends BrawlTest.Provider {
  override def newInstance(id: UUID, self: UUID) = new TestCrashTimerComplex(id, self)
}

class TestCrashTimerComplex(id: UUID, self: UUID) extends BrawlTest(id, self) {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, timer = Some(3)), 0, 0))
    test.board.applyMutation(AddGem(Gem(1, color = Color.Green, crash = Some(true)), 1, 0))
    test.board.applyMutation(AddGem(Gem(2, color = Color.Green, timer = Some(3)), 1, 1))

    test.board.applyMutation(AddGem(Gem(3, timer = Some(3)), 3, 0))
    test.board.applyMutation(AddGem(Gem(4), 4, 0))
    test.board.applyMutation(AddGem(Gem(5, timer = Some(3)), 3, 1))
    test.board.applyMutation(AddGem(Gem(6, timer = Some(3)), 4, 1))
    test.board.applyMutation(AddGem(Gem(7, crash = Some(true)), 5, 0))
    test.board.applyMutation(AddGem(Gem(8, timer = Some(3)), 5, 1))

    goal.board.applyMutation(AddGem(Gem(0, timer = Some(3)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(1, color = Color.Green, crash = Some(true)), 1, 0))
    goal.board.applyMutation(AddGem(Gem(2, color = Color.Green, timer = Some(3)), 1, 1))
    goal.board.applyMutation(AddGem(Gem(5, timer = Some(3)), 3, 0))
  }

  override def run() = {
    test.board.crash(None) ++ test.board.collapse().toSeq
  }
}
