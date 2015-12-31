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
    def newInstance(id: UUID, self: UUID): BrawlTest
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
    TestCrashTimer,
    TestCrashTimerComplex,
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

abstract class BrawlTest(id: UUID, self: UUID, val seed: Option[Int] = None) extends BrawlTestErrors {
  val brawl = Brawl.blank(
    id = id,
    scenario = this.getClass.getSimpleName.stripSuffix("$").replaceAllLiterally("Test", ""),
    seed = seed.getOrElse(Math.abs(Random.nextInt())),
    players = Seq(self -> "original", UUID.randomUUID -> "test", UUID.randomUUID -> "goal")
  )

  val original = brawl.players.find(_.name == "original").getOrElse(throw new IllegalStateException())
  val test = brawl.players.find(_.name == "test").getOrElse(throw new IllegalStateException())
  val goal = brawl.players.find(_.name == "goal").getOrElse(throw new IllegalStateException())

  def run(): Seq[UpdateSegment]

  def init(): Unit

  def cloneOriginal() = test.board.cloneTo(original.board)
}
