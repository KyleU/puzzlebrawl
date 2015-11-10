package models.benchmark

import java.util.concurrent.TimeUnit

import models.game.{ Gem, Board }
import org.openjdk.jmh.annotations._

class BoardBenchmark {
  @Benchmark
  @Measurement(iterations = 4, time = 5, timeUnit = TimeUnit.SECONDS)
  @Warmup(iterations = 2, time = 5, timeUnit = TimeUnit.SECONDS)
  @Threads(6)
  @Fork(1)
  def creation() = {
    val board = new Board(6, 12)
    board.add(Gem(0, Gem.Red), 0, 0)
    board.add(Gem(1, Gem.Red), 1, 0)
    board.add(Gem(2, Gem.Red), 2, 0)
    board.add(Gem(3, Gem.Red), 3, 0)
    board.add(Gem(4, Gem.Red), 4, 0)
    board
  }
}
