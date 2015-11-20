package models.game.test

import models.game.gem.GemStream

import scala.util.Random

object GameTestRandom extends GameTest.Provider {
  override def newInstance() = new GameTestRandom()
}

class GameTestRandom() extends GameTest(seed = Random.nextInt()) {
  override def init() = {
    val goalStream = GemStream(seed)
    for (y <- 0 until goal.height) {
      for (x <- 0 until goal.width) {
        goal.add(goalStream.next, x, y)
      }
    }
  }

  override def run() = {
    val boardStream = GemStream(seed)
    for (y <- 0 until board.height) {
      for (x <- 0 until board.width) {
        board.add(boardStream.next, x, y)
      }
    }
  }
}
