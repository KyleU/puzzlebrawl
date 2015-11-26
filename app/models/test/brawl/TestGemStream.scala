package models.test.brawl

import models.board.mutation.Mutation.AddGem
import models.gem.{ Gem, GemStream }

import scala.util.Random

object TestGemStream extends Test.Provider {
  override def newInstance() = new TestGemStream()
}

class TestGemStream() extends Test() {
  override def init() = {
    for (y <- 0 until 12) {
      for (x <- 0 until 6) {
        goal.board.applyMutation(AddGem(Gem((y * 6) + x), x, y))
      }
    }
  }

  override def run() = {
    val seed = Math.abs(Random.nextInt)
    val stream = GemStream(seed = seed, gemAdjustWild = 0, gemAdjustCrash = 0, gemAdjustRed = 1.0, gemAdjustGreen = 0, gemAdjustBlue = 0, gemAdjustYellow = 0)
    Seq((0 until 12).flatMap { y =>
      (0 until 6).map { x =>
        test.board.applyMutation(AddGem(stream.next, x, y))
      }
    })
  }
}
