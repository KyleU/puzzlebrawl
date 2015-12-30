package controllers.admin

import java.util.UUID

import controllers.BaseController
import models.board.Board
import models.board.mutation.UpdateSegment
import models.gem.GemStream
import models.player.Player
import models.test.brawl.BrawlTest
import play.api.libs.json.Json
import utils.{ Config, ApplicationContext, DateUtils }

import scala.concurrent.Future
import scala.util.control.NonFatal

object TestController {
  import utils.json.BrawlSerializers._
  import utils.json.MutationSerializers._

  private[this] def emptyPlayer(name: String, team: Int) = Player(UUID.randomUUID, name, team, Board(name, 0, 0), GemStream())

  case class Result(
    name: String,
    status: String,
    errors: Seq[BrawlTest.TestError],
    initMs: Int,
    runMs: Int,
    original: Player = emptyPlayer("original", 0),
    test: Player = emptyPlayer("test", 1),
    testMessages: Seq[UpdateSegment] = Seq.empty,
    goal: Player = emptyPlayer("goal", 2))
  implicit val writesResult = Json.writes[Result]
}

@javax.inject.Singleton
class TestController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  def list() = withAdminSession("test.list") { implicit request =>
    Future.successful(Ok(views.html.admin.test.list()))
  }

  def run(name: String, json: Boolean) = withAdminSession("test.run") { implicit request =>
    if (name == "All") {
      val results = BrawlTest.all.map(x => getResult(x.testName, x.newInstance(request.identity.id, UUID.randomUUID)))
      Future.successful(Ok(views.html.admin.test.testResultAll(results)))
    } else {
      Future.successful {
        val test = BrawlTest.fromString(name).map { x =>
          x.testName -> x.newInstance(request.identity.id, UUID.randomUUID)
        }.getOrElse(throw new IllegalArgumentException(s"Invalid test [$name]."))
        val result = getResult(test._1, test._2)
        val html = views.html.admin.test.testResult(result)
        if (json) {
          Ok(Json.toJson(result))
        } else {
          Ok(views.html.admin.layout.admin(s"${Config.projectName} [$name] Test", "test")(html))
        }
      }
    }
  }

  private[this] def getResult(testName: String, test: BrawlTest) = {
    val initStart = DateUtils.nowMillis
    val initError = try {
      test.init()
      test.cloneOriginal()
      None
    } catch {
      case NonFatal(x) =>
        log.warn(s"BrawlTest [$testName] has failed initialization with exception [$x].", x)
        Some((0, Seq.empty, Seq(BrawlTest.TestError(None, None, 0, 0, Some(s"${x.getClass.getSimpleName}: ${x.getMessage}")))))
    }
    val initMs = (DateUtils.nowMillis - initStart).toInt

    val (runMs, testMessages, testErrors) = initError.getOrElse {
      try {
        val runStart = DateUtils.nowMillis
        val msgs = test.run()
        val runMs = (DateUtils.nowMillis - runStart).toInt
        (runMs, msgs, test.getErrors)
      } catch {
        case NonFatal(x) =>
          log.warn(s"BrawlTest [$testName] has failed with exception [$x].", x)
          (0, Seq.empty, Seq(BrawlTest.TestError(None, None, 0, 0, Some(s"${x.getClass.getSimpleName}: ${x.getMessage}"))))
      }
    }

    val status = testErrors.headOption.map(x => s"${testErrors.size} Errors").getOrElse("Success")

    TestController.Result(testName, status, testErrors, initMs, runMs, test.original, test.test, testMessages, test.goal)
  }
}
