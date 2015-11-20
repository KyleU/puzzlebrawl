package controllers.admin

import controllers.BaseController
import models.game.test.GameTest
import play.api.i18n.MessagesApi
import services.user.AuthenticationEnvironment
import utils.DateUtils

import scala.concurrent.Future

object TestController {
  case class Result(
    name: String, status: String, errors: Seq[GameTest.TestError], initMs: Int, runMs: Int,
    original: models.game.Player, test: models.game.Player, goal: models.game.Player
  )
}

@javax.inject.Singleton
class TestController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def list() = withAdminSession("test.list") { implicit request =>
    Future.successful(Ok(views.html.admin.test.list()))
  }

  def run(name: String) = withAdminSession("test.run") { implicit request =>
    if (name == "All") {
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
    test.test.board.cloneTo(test.original.board)

    val (runMs, testErrors) = try {
      val runStart = DateUtils.nowMillis
      test.run()
      val runMs = (DateUtils.nowMillis - runStart).toInt
      runMs -> test.getErrors
    } catch {
      case x: Exception =>
        log.warn(s"Test [$testName] has failed with exception [$x].", x)
        0 -> Seq(GameTest.TestError(None, None, 0, 0, Some(s"${x.getClass.getSimpleName}: ${x.getMessage}")))
    }

    val status = testErrors.headOption.map(x => s"${testErrors.size} Errors").getOrElse("Success")

    TestController.Result(testName, status, testErrors, initMs, runMs, test.original, test.test, test.goal)
  }
}
