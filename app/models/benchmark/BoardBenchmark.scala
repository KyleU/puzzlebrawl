package models.benchmark

import java.util.UUID
import java.util.concurrent.TimeUnit
import models.board.mutation.Mutation.AddGem
import models.brawl.Brawl
import org.openjdk.jmh.annotations._

import scala.util.Random

class BoardBenchmark {
  @Benchmark
  @Measurement(iterations = 4, time = 5, timeUnit = TimeUnit.SECONDS)
  @Warmup(iterations = 2, time = 5, timeUnit = TimeUnit.SECONDS)
  @Threads(6)
  @Fork(1)
  def creation() = {
    val game = Brawl.blank(UUID.randomUUID)
    val p = game.players.headOption.getOrElse(throw new IllegalStateException())
    val board = p.board
    val gemStream = p.gemStream

    for (i <- 0 until 20) {
      val x = Random.nextInt(board.width)
      board.applyMutation(AddGem(gemStream.next, x, board.height - 1))
      board.drop(x, board.height - 1)
    }

    board.fuse()
    board.crash()
    board.collapse()
  }
}
