package models.test.brawl

import models.board.mutation.Mutation.AddGem
import models.gem.{ Color, Gem, GemLocation }

object TestActiveGemsInsert extends Test.Provider {
  override def newInstance() = new TestActiveGemsInsert()
}

class TestActiveGemsInsert() extends Test() {
  override def init() = {
    test.activeGems = Seq(GemLocation(Gem(0, color = Color.Yellow), 2, 11), GemLocation(Gem(1, color = Color.Blue), 3, 11))

    test.board.applyMutation(AddGem(Gem(2, width = Some(2), height = Some(3)), 0, 0))
    test.board.applyMutation(AddGem(Gem(3, color = Color.Blue, crash = Some(true)), 0, 3))
    test.board.applyMutation(AddGem(Gem(4, width = Some(2), height = Some(3)), 0, 4))

    goal.board.applyMutation(AddGem(Gem(2, width = Some(2), height = Some(6)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(0, color = Color.Yellow), 2, 0))
  }

  override def run() = {
    test.activeGemsClockwise()
    test.activeGemsClockwise()
    test.activeGemsRight()
    (0 until 8).foreach(x => test.activeGemsStep())
    test.activeGemsLeft()
    Seq(test.activeGemsDrop()) ++ test.board.fullTurn()
  }
}
