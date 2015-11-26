package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Gem, Color }

object GameTestCrashLarge extends GameTest.Provider {
  override def newInstance() = new GameTestCrashLarge()
}

class GameTestCrashLarge() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(3), height = Some(3)), 0, 0))
    test.board.applyMutation(AddGem(Gem(1, Color.Blue), 3, 0))
    test.board.applyMutation(AddGem(Gem(2, width = Some(2), height = Some(2)), 3, 1))
    test.board.applyMutation(AddGem(Gem(3, crash = Some(true)), 5, 0))
    test.board.applyMutation(AddGem(Gem(4), 5, 1))
    test.board.applyMutation(AddGem(Gem(5), 1, 3))
    test.board.applyMutation(AddGem(Gem(6, width = Some(2), height = Some(2)), 0, 4))

    goal.board.applyMutation(AddGem(Gem(1, Color.Blue), 3, 0))
  }

  override def run() = {
    test.board.crash() :+ test.board.collapse()
  }
}
