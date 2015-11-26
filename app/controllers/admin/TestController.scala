package controllers.admin

import controllers.BaseController
import models.board.mutation.Mutation
import models.player.Player
import models.test.brawl.Test
import Test.TestError
import models.test.brawl.Test
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import services.user.AuthenticationEnvironment
import utils.DateUtils

import scala.concurrent.Future

object TestController {
  import utils.json.GameSerializers._
  import utils.json.MutationSerializers._

  case class Result(
    name: String,
    status: String,
    errors: Seq[TestError],
    initMs: Int,
    runMs: Int,
    original: Player,
    test: Player,
    testMessages: Seq[Seq[Mutation]],
    goal: Player
  )
  implicit val writesResult = Json.writes[Result]
}

@javax.inject.Singleton
class TestController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def list() = withAdminSession("test.list") { implicit request =>
    Future.successful(Ok(views.html.admin.test.list()))
  }

  def run(name: String, json: Boolean) = withAdminSession("test.run") { implicit request =>
    if (name == "All") {
      val results = Test.all.map(x => getResult(x.testName, x.newInstance()))
      Future.successful(Ok(views.html.admin.test.testResultAll(results)))
    } else {
      Future.successful {
        val test = Test.fromString(name).map(x => x.testName -> x.newInstance()).getOrElse(throw new IllegalArgumentException(s"Invalid test [$name]."))
        val result = getResult(test._1, test._2)
        val html = views.html.admin.test.testResult(result)
        if(json) {
          Ok(Json.toJson(result))
        } else {
          Ok(views.html.layout.admin(s"${utils.Config.projectName} [$name] Test", "test")(html))
        }
      }
    }
  }

  private[this] def getResult(testName: String, test: Test) = {
    val initStart = DateUtils.nowMillis
    test.init()
    val initMs = (DateUtils.nowMillis - initStart).toInt
    test.test.board.cloneTo(test.original.board)

    val (runMs, testMessages, testErrors) = try {
      val runStart = DateUtils.nowMillis
      val msgs = test.run()
      val runMs = (DateUtils.nowMillis - runStart).toInt
      (runMs, msgs, test.getErrors)
    } catch {
      case x: Exception =>
        log.warn(s"Test [$testName] has failed with exception [$x].", x)
        (0, Seq.empty, Seq(Test.TestError(None, None, 0, 0, Some(s"${x.getClass.getSimpleName}: ${x.getMessage}"))))
    }

    val status = testErrors.headOption.map(x => s"${testErrors.size} Errors").getOrElse("Success")

    TestController.Result(testName, status, testErrors, initMs, runMs, test.original, test.test, testMessages, test.goal)
  }
}
