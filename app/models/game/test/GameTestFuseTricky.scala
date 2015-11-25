package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestFuseTricky extends GameTest.Provider {
  override def newInstance() = new GameTestFuseTricky()
}

class GameTestFuseTricky() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))
    test.board.applyMutation(AddGem(Gem(1), 0, 2))
    test.board.applyMutation(AddGem(Gem(2), 1, 2))

    test.board.applyMutation(AddGem(Gem(3), 2, 0))
    test.board.applyMutation(AddGem(Gem(4), 3, 0))
    test.board.applyMutation(AddGem(Gem(5, width = Some(2), height = Some(2)), 2, 1))

    test.board.applyMutation(AddGem(Gem(6, width = Some(2), height = Some(2)), 4, 0))
    test.board.applyMutation(AddGem(Gem(7), 4, 2))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(4), height = Some(3)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(6, width = Some(2), height = Some(2)), 4, 0))
    goal.board.applyMutation(AddGem(Gem(7), 4, 2))
  }

  override def run() = test.board.fuse()
}
