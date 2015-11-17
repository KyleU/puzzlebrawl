package models.game.test

import models.game.board.Board
import models.game.gem.Gem
import play.api.libs.json.Json

import scala.util.Random

object GameTest {
  import utils.json.GameSerializers._

  trait Provider {
    val testName = this.getClass.getSimpleName.stripSuffix("$").replaceAllLiterally("GameTest", "")
    def newInstance(): GameTest
  }

  val all = Seq(
    GameTestCollapse,
    GameTestCrash,
    GameTestDrop,
    GameTestFuseGroups,
    GameTestFuseBasic,
    GameTestGemStream,
    GameTestRandom,
    GameTestRemove,
    GameTestTimer
  )
  def fromString(s: String) = all.find(_.testName == s)

  case class TestError(src: Option[Gem], tgt: Option[Gem], x: Int, y: Int) {
    override def toString = s"[$x, $y]: ${src.getOrElse("Empty")} is not equal to ${tgt.getOrElse("Empty")}."
  }
  implicit val testErrorReads = Json.reads[TestError]
  implicit val testErrorWrites = Json.writes[TestError]
}

abstract class GameTest(val seed: Int = Math.abs(Random.nextInt()), val board: Board = Board.withKey("board"), val goal: Board = Board.withKey("goal")) {
  protected val r = new Random(seed)

  def run(): Unit

  def init(): Unit

  def getErrors = (0 until goal.width).flatMap { x =>
    (0 until goal.height).flatMap { y =>
      val src = board.at(x, y)
      val tgt = goal.at(x, y)
      if(src == tgt) {
        None
      } else {
        Some(GameTest.TestError(src, tgt, x, y))
      }
    }
  }
}
