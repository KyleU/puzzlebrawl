package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestCombo extends GameTest.Provider {
  override def newInstance() = new GameTestCombo()
}

class GameTestCombo() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0), 0, 0))
    test.board.applyMutation(AddGem(Gem(1), 1, 0))
    test.board.applyMutation(AddGem(Gem(2, crash = true), 0, 1))
    test.board.applyMutation(AddGem(Gem(3, Color.Green), 1, 1))
    test.board.applyMutation(AddGem(Gem(4, Color.Green, crash = true), 0, 2))
    test.board.applyMutation(AddGem(Gem(5, Color.Blue), 1, 2))
    test.board.applyMutation(AddGem(Gem(6, Color.Blue, crash = true), 2, 0))
    test.board.applyMutation(AddGem(Gem(7, Color.Yellow), 2, 1))
    test.board.applyMutation(AddGem(Gem(8, Color.Yellow, crash = true), 3, 0))
  }

  override def run() = {
    test.board.fullTurn()
  }
}
