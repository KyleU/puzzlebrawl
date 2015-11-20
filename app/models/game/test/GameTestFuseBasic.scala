package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestFuseBasic extends GameTest.Provider {
  override def newInstance() = new GameTestFuseBasic()
}

class GameTestFuseBasic() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, Color.Red), 0, 0))
    test.board.applyMutation(AddGem(Gem(1, Color.Red), 1, 0))
    test.board.applyMutation(AddGem(Gem(2, Color.Red), 2, 0))
    test.board.applyMutation(AddGem(Gem(3, Color.Red), 0, 1))
    test.board.applyMutation(AddGem(Gem(4, Color.Red), 1, 1))
    test.board.applyMutation(AddGem(Gem(5, Color.Red), 2, 1))
    test.board.applyMutation(AddGem(Gem(6, Color.Red), 0, 2))
    test.board.applyMutation(AddGem(Gem(7, Color.Red), 1, 2))
    test.board.applyMutation(AddGem(Gem(8, Color.Red), 2, 2))
    test.board.applyMutation(AddGem(Gem(9, Color.Red), 0, 3))
    test.board.applyMutation(AddGem(Gem(10, Color.Red), 3, 0))
    test.board.applyMutation(AddGem(Gem(11, Color.Red), 3, 1))
    test.board.applyMutation(AddGem(Gem(12, Color.Red, timer = Some(5)), 3, 2))

    goal.board.applyMutation(AddGem(Gem(0, Color.Red, width = Some(3), height = Some(3)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(9, Color.Red), 0, 3))
    goal.board.applyMutation(AddGem(Gem(10, Color.Red), 3, 0))
    goal.board.applyMutation(AddGem(Gem(11, Color.Red), 3, 1))
    goal.board.applyMutation(AddGem(Gem(12, Color.Red, timer = Some(5)), 3, 2))
  }

  override def run() = test.board.fuse()
}
