package models.game.test

import models.game.board.mutation.Mutation.RemoveGem
import models.game.gem.{ Color, Gem }

object GameTestRemove extends GameTest.Provider {
  override def newInstance() = new GameTestRemove()
}

class GameTestRemove() extends GameTest(seed = 0) {
  override def init() = {
    board.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)
    board.add(Gem(0, Color.Green), 2, 0)
    board.add(Gem(0, Color.Blue, width = Some(3), height = Some(3)), 3, 0)
  }

  override def run() = {
    board.applyMutation(RemoveGem(0, 0))
    board.applyMutation(RemoveGem(2, 0))
    board.applyMutation(RemoveGem(4, 1))
  }
}
