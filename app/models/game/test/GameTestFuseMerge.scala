package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestFuseMerge extends GameTest.Provider {
  override def newInstance() = new GameTestFuseMerge()
}

class GameTestFuseMerge() extends GameTest() {
  override def init() = {
    // Group 1
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))

    // Group 2
    test.board.applyMutation(AddGem(Gem(1, width = Some(2), height = Some(2)), 2, 0))

    // Additions
    test.board.applyMutation(AddGem(Gem(2), 4, 0))
    test.board.applyMutation(AddGem(Gem(3), 4, 1))

    // Lonely Single
    test.board.applyMutation(AddGem(Gem(4), 5, 0))

    // Goal
    goal.board.applyMutation(AddGem(Gem(0, width = Some(5), height = Some(2)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(4), 5, 0))
  }

  override def run() = test.board.fuse()
}
