package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestFuseTrickyTwo extends GameTest.Provider {
  override def newInstance() = new GameTestFuseTrickyTwo()
}

class GameTestFuseTrickyTwo() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(4), height = Some(2)), 0, 0))

    test.board.applyMutation(AddGem(Gem(1), 2, 2))
    test.board.applyMutation(AddGem(Gem(2), 3, 2))
    test.board.applyMutation(AddGem(Gem(3, width = Some(2), height = Some(2)), 2, 3))

    test.board.applyMutation(AddGem(Gem(4, width = Some(2), height = Some(2)), 0, 2))
    test.board.applyMutation(AddGem(Gem(5), 0, 4))
    test.board.applyMutation(AddGem(Gem(6), 1, 4))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(4), height = Some(5)), 0, 0))
  }

  override def run() = test.board.fuse()
}
