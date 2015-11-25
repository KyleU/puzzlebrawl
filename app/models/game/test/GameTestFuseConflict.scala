package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestFuseConflict extends GameTest.Provider {
  override def newInstance() = new GameTestFuseConflict()
}

class GameTestFuseConflict() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))
    test.board.applyMutation(AddGem(Gem(1, width = Some(3), height = Some(3)), 2, 0))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(1, width = Some(3), height = Some(3)), 2, 0))
  }

  override def run() = test.board.fuse()
}
