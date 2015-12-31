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

    goal.board.applyMutation(AddGem(Gem(0, timer = Some(3)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(1, color = Color.Green, crash = Some(true)), 1, 0))
  }

  override def run() = {
    test.board.crash(None) ++ test.board.collapse().toSeq
  }
}
