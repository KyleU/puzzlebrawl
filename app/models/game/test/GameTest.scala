package models.game.test

import models.game.Game
import models.game.board.mutation.Mutation
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
    GameTestClear,
    GameTestCollapse,
    GameTestCombo,
    GameTestCrash,
    GameTestDrop,
    GameTestDropFull,
    GameTestFullTurn,
    GameTestFuseBasic,
    GameTestFuseConflict,
    GameTestFuseExpand,
    GameTestFuseMerge,
    GameTestFuseTricky,
    GameTestFuseTrickyTwo,
    GameTestFuseTrickyThree,
    GameTestGemStream,
    GameTestRandom,
    GameTestRemove,
    GameTestTestbed,
    GameTestTimer,
    GameTestWild
  )
  def fromString(s: String) = all.find(_.testName == s)

  case class TestError(src: Option[Gem], tgt: Option[Gem], x: Int, y: Int, message: Option[String] = None) {
    override def toString = s"[$x, $y]: " + message.getOrElse(s"${src.getOrElse("Empty")} is not equal to ${tgt.getOrElse("Empty")}.")
  }
  implicit val testErrorReads = Json.reads[TestError]
  implicit val testErrorWrites = Json.writes[TestError]
}

abstract class GameTest(val seed: Option[Int] = None) {
  val game = Game.blank(seed = seed.getOrElse(Math.abs(Random.nextInt())), playerNames = Seq("original", "test", "goal"))

  val original = game.players.find(_.name == "original").getOrElse(throw new IllegalStateException())
  val test = game.players.find(_.name == "test").getOrElse(throw new IllegalStateException())
  val goal = game.players.find(_.name == "goal").getOrElse(throw new IllegalStateException())

  def run(): Seq[Seq[Mutation]]

  def init(): Unit

  def getErrors = (0 until goal.board.height).flatMap { y =>
    (0 until goal.board.width).flatMap { x =>
      val src = test.board.at(x, y)
      val tgt = goal.board.at(x, y)
      if (src == tgt) {
        None
      } else {
        Some(GameTest.TestError(src, tgt, x, y))
      }
    }
  }
}
