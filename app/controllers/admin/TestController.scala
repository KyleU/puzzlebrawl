package controllers.admin

import controllers.BaseController
import models.game.gem.Gem
import models.game.test.GameTest
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import services.user.AuthenticationEnvironment
import utils.DateUtils
import utils.json.GameSerializers._

import scala.concurrent.Future

@javax.inject.Singleton
class TestController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def list() = withAdminSession("test.list") { implicit request =>
    val files = GameTest.all.map(_.testName)
    Future.successful(Ok(views.html.admin.test.list()))
  }

  def run(name: String) = withAdminSession("test.run") { implicit request =>
    val test = GameTest.fromString(name).map(_.newInstance()).getOrElse(throw new IllegalArgumentException(s"Invalid test [$name]."))

    val initStart = DateUtils.nowMillis
    test.init()
    val initMs = (DateUtils.nowMillis - initStart).toInt

    val runStart = DateUtils.nowMillis
    test.run()
    val runMs = (DateUtils.nowMillis - runStart).toInt

    val testErrors = test.getErrors
    val status = testErrors.headOption.map( x => s"${testErrors.size} Errors").getOrElse("Success")
    val board = Json.toJson(test.board)
    val goal = Json.toJson(test.goal)

    Future.successful(Ok(views.html.admin.test.result(name, status, testErrors, initMs, runMs, test.board, test.goal)))
  }
}
