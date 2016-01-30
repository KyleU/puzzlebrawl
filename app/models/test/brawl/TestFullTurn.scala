package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.AddGem

import scala.util.Random

object TestFullTurn extends BrawlTest.Provider {
  override def newInstance(id: UUID, self: UUID) = new TestFullTurn(id, self)
}

class TestFullTurn(id: UUID, self: UUID) extends BrawlTest(id, self, seed = Some(Random.nextInt())) {
  override def init() = {
    for (y <- 0 until test.board.height) {
      for (x <- 0 until test.board.width) {
        test.board.applyMutation(AddGem(test.gemStream.next(), x, y))
      }
    }

    for (y <- 0 until goal.board.height) {
      for (x <- 0 until goal.board.width) {
        goal.board.applyMutation(AddGem(goal.gemStream.next(), x, y))
      }
    }
    goal.board.processWilds() ++ goal.board.fullTurn()
  }

  override def run() = {
    test.board.processWilds() ++ test.board.fullTurn()
  }
}
