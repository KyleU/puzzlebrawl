package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestFuseBasic extends GameTest.Provider {
  override def newInstance() = new GameTestFuseBasic()
}

class GameTestFuseBasic() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0), 0, 0))
    test.board.applyMutation(AddGem(Gem(1), 1, 0))
    test.board.applyMutation(AddGem(Gem(2), 2, 0))
    test.board.applyMutation(AddGem(Gem(3), 0, 1))
    test.board.applyMutation(AddGem(Gem(4), 1, 1))
    test.board.applyMutation(AddGem(Gem(5), 2, 1))
    test.board.applyMutation(AddGem(Gem(6), 0, 2))
    test.board.applyMutation(AddGem(Gem(7), 1, 2))
    test.board.applyMutation(AddGem(Gem(8), 2, 2))
    test.board.applyMutation(AddGem(Gem(9), 0, 3))
    test.board.applyMutation(AddGem(Gem(10), 3, 0))
    test.board.applyMutation(AddGem(Gem(11), 3, 1))
    test.board.applyMutation(AddGem(Gem(12, timer = Some(5)), 3, 2))

    goal.board.applyMutation(AddGem(Gem(0, width = Some(3), height = Some(3)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(9), 0, 3))
    goal.board.applyMutation(AddGem(Gem(10), 3, 0))
    goal.board.applyMutation(AddGem(Gem(11), 3, 1))
    goal.board.applyMutation(AddGem(Gem(12, timer = Some(5)), 3, 2))
  }

  override def run() = test.board.fuse()
}
