package models.game.test

import models.game.board.mutation.Mutation.AddGem
import models.game.gem.{ Color, Gem }

object GameTestTimer extends GameTest.Provider {
  override def newInstance() = new GameTestTimer()
}

class GameTestTimer() extends GameTest() {
  override def init() = {
    test.board.applyMutation(AddGem(Gem(0, timer = Some(5)), 0, 0))
    test.board.applyMutation(AddGem(Gem(0, timer = Some(4)), 1, 0))
    test.board.applyMutation(AddGem(Gem(0, timer = Some(3)), 2, 0))
    test.board.applyMutation(AddGem(Gem(0, timer = Some(2)), 3, 0))
    test.board.applyMutation(AddGem(Gem(0, timer = Some(1)), 4, 0))
    test.board.applyMutation(AddGem(Gem(0), 5, 0))

    goal.board.applyMutation(AddGem(Gem(0, timer = Some(4)), 0, 0))
    goal.board.applyMutation(AddGem(Gem(0, timer = Some(3)), 1, 0))
    goal.board.applyMutation(AddGem(Gem(0, timer = Some(2)), 2, 0))
    goal.board.applyMutation(AddGem(Gem(0, timer = Some(1)), 3, 0))
    goal.board.applyMutation(AddGem(Gem(0), 4, 0))
    goal.board.applyMutation(AddGem(Gem(0), 5, 0))
  }

  override def run() = Seq(test.board.decrementTimers())
}
