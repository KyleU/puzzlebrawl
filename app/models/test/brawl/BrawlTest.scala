package models.test.brawl

import java.util.UUID

import models.board.mutation.UpdateSegment
import models.brawl.Brawl
import models.gem.Gem
import play.api.libs.json.Json

import scala.util.Random

object BrawlTest {
  import utils.json.BrawlSerializers._

  trait Provider {
    val testName = this.getClass.getSimpleName.stripSuffix("$").replaceAllLiterally("Test", "")
    def newInstance(id: UUID): BrawlTest
  }

  val all = Seq(
    TestActiveGemsDrop,
    TestActiveGemsInsert,
    TestActiveGemsRotate,
    TestClear,
    TestCollapse,
    TestCombo,
    TestCrash,
    TestCrashLarge,
    TestDrop,
    TestDropFull,
    TestFullTurn,
    TestFuseBasic,
    TestFuseConflict,
    TestFuseExpand,
    TestFuseMerge,
    TestFuseTricky,
    TestFuseTrickyTwo,
    TestFuseTrickyThree,
    TestGemStream,
    TestRandom,
    TestRemove,
    TestScratchpad,
    TestTimer,
    TestWild
  )
  def fromString(s: String) = all.find(_.testName == s)

  case class TestError(src: Option[Gem], tgt: Option[Gem], x: Int, y: Int, message: Option[String] = None) {
    override def toString = s"[$x, $y]: " + message.getOrElse(s"${src.getOrElse("Empty")} is not equal to ${tgt.getOrElse("Empty")}.")
  }
  implicit val testErrorReads = Json.reads[TestError]
  implicit val testErrorWrites = Json.writes[TestError]
}

abstract class BrawlTest(id: UUID, val seed: Option[Int] = None) {
  val brawl = Brawl.blank(
    id = id,
    scenario = this.getClass.getSimpleName.stripSuffix("$").replaceAllLiterally("Test", ""),
    seed = seed.getOrElse(Math.abs(Random.nextInt())),
    players = Seq(UUID.randomUUID -> "original", UUID.randomUUID -> "test", UUID.randomUUID -> "goal")
  )

  val original = brawl.players.find(_.name == "original").getOrElse(throw new IllegalStateException())
  val test = brawl.players.find(_.name == "test").getOrElse(throw new IllegalStateException())
  val goal = brawl.players.find(_.name == "goal").getOrElse(throw new IllegalStateException())

  def run(): Seq[UpdateSegment]

  def init(): Unit

  def cloneOriginal() = test.board.cloneTo(original.board)

  def getErrors = {
    val spaceErrors = (0 until goal.board.height).flatMap { y =>
      (0 until goal.board.width).flatMap { x =>
        val src = test.board.at(x, y)
        val tgt = goal.board.at(x, y)
        if (src == tgt) {
          None
        } else {
          Some(BrawlTest.TestError(src, tgt, x, y))
        }
      }
    }
    val activeGemErrors = test.activeGems.indices.flatMap { i =>
      val src = test.activeGems.lift(i)
      val tgt = goal.activeGems.lift(i)
      if (src == tgt) {
        None
      } else {
        Some(BrawlTest.TestError(src.map(_.gem), tgt.map(_.gem), src.map(_.x).getOrElse(0), src.map(_.y).getOrElse(0)))
      }
    }
    spaceErrors ++ activeGemErrors
  }
}
