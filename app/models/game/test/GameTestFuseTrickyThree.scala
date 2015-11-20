package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestFuseTrickyThree extends GameTest.Provider {
  override def newInstance() = new GameTestFuseTrickyThree()
}

class GameTestFuseTrickyThree() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, Color.Red, width = Some(2), height = Some(4)), 0, 0))

    test.board.applyMutation(AddGem(Gem(1, Color.Red, width = Some(2), height = Some(2)), 2, 2))
    test.board.applyMutation(AddGem(Gem(2, Color.Red), 4, 2))
    test.board.applyMutation(AddGem(Gem(3, Color.Red), 4, 3))

    test.board.applyMutation(AddGem(Gem(4, Color.Red), 2, 0))
    test.board.applyMutation(AddGem(Gem(5, Color.Red, width = Some(2), height = Some(2)), 3, 0))
    test.board.applyMutation(AddGem(Gem(6, Color.Red), 2, 1))

    goal.board.applyMutation(AddGem(Gem(0, Color.Red, width = Some(5), height = Some(4)), 0, 0))
  }

  override def run() = test.board.fuse()
}
