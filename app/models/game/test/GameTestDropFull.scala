package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestDropFull extends GameTest.Provider {
  override def newInstance() = new GameTestDropFull()
}

class GameTestDropFull() extends GameTest() {
  override def init() = {
    (0 until goal.board.height).foreach(i => goal.board.applyMutation(AddGem(goal.gemStream.next, 0, i)))
  }

  override def run() = {
    (0 until test.board.height).foreach(i => test.board.drop(test.gemStream.next, 0))
  }
}
