package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestFuseTrickyThree extends GameTest.Provider {
  override def newInstance() = new GameTestFuseTrickyThree()
}

class GameTestFuseTrickyThree() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(4)), 0, 0))

    test.board.applyMutation(AddGem(Gem(1, width = Some(2), height = Some(2)), 2, 2))
    test.board.applyMutation(AddGem(Gem(2), 4, 2))
    test.board.applyMutation(AddGem(Gem(3), 4, 3))

    test.board.applyMutation(AddGem(Gem(4), 2, 0))
    test.board.applyMutation(AddGem(Gem(5, width = Some(2), height = Some(2)), 3, 0))
    test.board.applyMutation(AddGem(Gem(6), 2, 1))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(5), height = Some(4)), 0, 0))
  }

  override def run() = test.board.fuse()
}
