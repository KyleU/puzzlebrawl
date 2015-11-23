package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestCrash extends GameTest.Provider {
  override def newInstance() = new GameTestCrash()
}

class GameTestCrash() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, Color.Red), 0, 0))
    test.board.applyMutation(AddGem(Gem(1, Color.Red, crash = true), 1, 0))
    test.board.applyMutation(AddGem(Gem(2, Color.Red), 2, 0))
    test.board.applyMutation(AddGem(Gem(3, Color.Red), 2, 1))
    test.board.applyMutation(AddGem(Gem(4, Color.Red), 3, 1))
    test.board.applyMutation(AddGem(Gem(5, Color.Red, width = Some(2), height = Some(2)), 4, 0))
    test.board.applyMutation(AddGem(Gem(6, Color.Blue), 0, 1))
    test.board.applyMutation(AddGem(Gem(7, Color.Green), 3, 2))
    test.board.applyMutation(AddGem(Gem(8, Color.Red), 5, 2))
    test.board.applyMutation(AddGem(Gem(9, Color.Blue), 3, 0))
    //test.board.applyMutation(AddGem(Gem(10, Color.Red), 5, 2))

    goal.board.applyMutation(AddGem(Gem(6, Color.Blue), 0, 0))
    goal.board.applyMutation(AddGem(Gem(7, Color.Green), 3, 1))
    goal.board.applyMutation(AddGem(Gem(9, Color.Blue), 3, 0))
  }

  override def run() = {
    test.board.crash() :+ test.board.collapse()
  }
}
