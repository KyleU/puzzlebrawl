package controllers.admin

import controllers.BaseController
import models.game.board.Board
import models.game.test.GameTest
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import services.user.AuthenticationEnvironment
import utils.DateUtils
import utils.json.GameSerializers._

import scala.concurrent.Future

object TestController {
  case class Result(
    name: String, status: String, errors: Seq[models.game.test.GameTest.TestError], initMs: Int, runMs: Int,
    original: models.game.board.Board, board: models.game.board.Board, goal: models.game.board.Board
  )
}

@javax.inject.Singleton
class TestController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def list() = withAdminSession("test.list") { implicit request =>
    val files = GameTest.all.map(_.testName)
    Future.successful(Ok(views.html.admin.test.list()))
  }

  def run(name: String) = withAdminSession("test.run") { implicit request =>
    if(name == "All") {
      val results = GameTest.all.map(x => getResult(x.testName, x.newInstance()))
      Future.successful(Ok(views.html.admin.test.testResultAll(results)))
    } else {
      Future.successful {
        val test = GameTest.fromString(name).map(x => x.testName -> x.newInstance()).getOrElse(throw new IllegalArgumentException(s"Invalid test [$name]."))
        val result = views.html.admin.test.testResult(getResult(test._1, test._2))
        Ok(views.html.layout.admin(s"${utils.Config.projectName} [$name] Test", "test")(result))
      }
    }
  }

  private[this] def getResult(testName: String, test: GameTest) = {
    val initStart = DateUtils.nowMillis
    test.init()
    val initMs = (DateUtils.nowMillis - initStart).toInt
    val originalBoard = Board("original", test.board.spaces.map(_.clone()))

    val runStart = DateUtils.nowMillis
    test.run()
    val runMs = (DateUtils.nowMillis - runStart).toInt

    val testErrors = test.getErrors
    val status = testErrors.headOption.map( x => s"${testErrors.size} Errors").getOrElse("Success")
    val board = Json.toJson(test.board)
    val goal = Json.toJson(test.goal)

    TestController.Result(testName, status, testErrors, initMs, runMs, originalBoard, test.board, test.goal)
  }
}
