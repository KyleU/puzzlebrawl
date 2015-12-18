package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.AddGem
import models.gem.{ Color, Gem }

object TestCollapse extends BrawlTest.Provider {
  override def newInstance(id: UUID, self: UUID) = new TestCollapse(id, self)
}

class TestCollapse(id: UUID, self: UUID) extends BrawlTest(id, self) {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 1))
    test.board.applyMutation(AddGem(Gem(1, Color.Green), 1, 4))
    test.board.applyMutation(AddGem(Gem(2, Color.Blue), 2, 5))
    test.board.applyMutation(AddGem(Gem(3, Color.Blue), 3, 7))
    test.board.applyMutation(AddGem(Gem(4, Color.Green), 4, 9))
    test.board.applyMutation(AddGem(Gem(5), 5, 11))
    test.board.applyMutation(AddGem(Gem(6, Color.Yellow), 2, 6))
    test.board.applyMutation(AddGem(Gem(7, Color.Yellow), 3, 9))
    test.board.applyMutation(AddGem(Gem(8, Color.Blue, width = Some(2), height = Some(2)), 0, 10))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(1, Color.Green), 1, 2))
    goal.board.applyMutation(AddGem(Gem(2, Color.Blue), 2, 0))
    goal.board.applyMutation(AddGem(Gem(3, Color.Blue), 3, 0))
    goal.board.applyMutation(AddGem(Gem(4, Color.Green), 4, 0))
    goal.board.applyMutation(AddGem(Gem(5), 5, 0))
    goal.board.applyMutation(AddGem(Gem(6, Color.Yellow), 2, 1))
    goal.board.applyMutation(AddGem(Gem(7, Color.Yellow), 3, 1))
    goal.board.applyMutation(AddGem(Gem(8, Color.Blue, width = Some(2), height = Some(2)), 0, 3))
  }

  override def run() = test.board.collapse().toSeq
}
