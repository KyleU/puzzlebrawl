package models.game.test

import models.game.gem.{ GemStream, Color, Gem }

import scala.util.Random

object GameTestRandom extends GameTest.Provider {
  override def newInstance() = GameTestRandom()
}

case class GameTestRandom() extends GameTest(seed = Random.nextInt()) {
  private val boardStream = GemStream(seed)
  private val goalStream = GemStream(seed)

  override def init() = for(y <- 0 until goal.height) {
    for(x <- 0 until goal.width) {
      goal.add(goalStream.next, x, y)
    }
  }

  override def run() = for(y <- 0 until board.height) {
    for(x <- 0 until board.width) {
      board.add(goalStream.next, x, y)
    }
  }
}
