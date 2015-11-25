package models.game.test

import models.game.board.mutation.Mutation.{ AddGem, RemoveGem }
import models.game.gem.{ Color, Gem }

object GameTestRemove extends GameTest.Provider {
  override def newInstance() = new GameTestRemove()
}

class GameTestRemove() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, width = Some(2), height = Some(2)), 0, 0))
    test.board.applyMutation(AddGem(Gem(0, Color.Green), 2, 0))
    test.board.applyMutation(AddGem(Gem(0, Color.Blue, width = Some(3), height = Some(3)), 3, 0))
  }

  override def run() = Seq(Seq(
    test.board.applyMutation(RemoveGem(0, 0)),
    test.board.applyMutation(RemoveGem(2, 0)),
    test.board.applyMutation(RemoveGem(4, 1))
  ))
}
