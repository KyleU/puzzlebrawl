package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.{ AddGem, RemoveGem }
import models.board.mutation.UpdateSegment
import models.gem.{ Color, Gem }

object TestRemove extends BrawlTest.Provider {
  override def newInstance(id: UUID) = new TestRemove(id)
}

class TestRemove(id: UUID) extends BrawlTest(id) {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))
    test.board.applyMutation(AddGem(Gem(0, Color.Green), 2, 0))
    test.board.applyMutation(AddGem(Gem(0, Color.Blue, width = Some(3), height = Some(3)), 3, 0))
  }

  override def run() = Seq(UpdateSegment("remove", Seq(
    test.board.applyMutation(RemoveGem(0, 0)),
    test.board.applyMutation(RemoveGem(2, 0)),
    test.board.applyMutation(RemoveGem(4, 1))
  )))
}
