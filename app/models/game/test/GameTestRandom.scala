package models.game.test

object GameTestRandom extends GameTest.Provider {
  override def newInstance() = new GameTestRandom()
}

class GameTestRandom() extends GameTest() {
  override def init() = {
    for (y <- 0 until goal.board.height) {
      for (x <- 0 until goal.board.width) {
        goal.board.add(goal.gemStream.next, x, y)
      }
    }
  }

  override def run() = {
    for (y <- 0 until test.board.height) {
      for (x <- 0 until test.board.width) {
        test.board.add(test.gemStream.next, x, y)
      }
    }
  }
}
