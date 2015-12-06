package models.test.brawl

import java.util.UUID

import models.board.mutation.Mutation.AddGem
import models.board.mutation.UpdateSegment
import models.gem.{ Gem, GemStream }

import scala.util.Random

object TestGemStream extends BrawlTest.Provider {
  override def newInstance(id: UUID) = new TestGemStream(id)
}

class TestGemStream(id: UUID) extends BrawlTest(id) {
  override def init() = {
    for (y <- 0 until 12) {
      for (x <- 0 until 6) {
        goal.board.applyMutation(AddGem(Gem((y * 6) + x), x, y))
      }
    }
  }

  override def run() = {
    val seed = Math.abs(Random.nextInt)
    val stream = GemStream(
      seed = seed,
      gemAdjustWild = Some(0),
      gemAdjustCrash = Some(0),
      gemAdjustRed = Some(1.0),
      gemAdjustGreen = Some(0),
      gemAdjustBlue = Some(0),
      gemAdjustYellow = Some(0)
    )
    Seq(UpdateSegment(
      "stream",
      Seq((0 until 12).flatMap { y =>
        (0 until 6).map { x =>
          test.board.applyMutation(AddGem(stream.next, x, y))
        }
      }).flatten
    ))
  }
}
