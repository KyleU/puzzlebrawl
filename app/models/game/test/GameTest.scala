package models.game.test

import models.game.board.Board

import scala.util.Random

object GameTest {
  val all = Seq(GameTestCollapse, GameTestCrash, GameTestFuse)
  def fromString(s: String) = all.find(_.testName == s)
}

abstract class GameTest(val seed: Int = Math.abs(Random.nextInt()), val width: Int = 6, val height: Int = 12) {
  val testName = this.getClass.getSimpleName.stripSuffix("$").replaceAllLiterally("GameTest", "")
  protected val r = new Random(seed)

  val board = Board("board", width, height)
  val testBoard = Board("test", width, height)

  def run(): Unit

  def init(): Unit
}
