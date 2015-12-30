package controllers.admin

import akka.pattern.ask
import akka.util.Timeout
import controllers.BaseController
import models._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Schema
import services.user.UserService
import utils.ApplicationContext

import scala.concurrent.Future
import scala.concurrent.duration._

@javax.inject.Singleton
class AdminController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def enable = withSession("admin.enable") { implicit request =>
    UserService.enableAdmin(request.identity).map { response =>
      Ok(response)
    }
  }

  def index = withAdminSession("index") { implicit request =>
    (ctx.supervisor ask GetSystemStatus).map {
      case x: SystemStatus => Ok(views.html.admin.activity.status(x))
    }
  }

  def constants = withAdminSession("constants") { implicit request =>
    Future.successful(Ok(views.html.admin.report.constants()))
  }

  def wipeSchema(key: String) = withAdminSession("wipe.schema") { implicit request =>
    if (key == "SuperSecretPassword") {
      Schema.wipe().map { tables =>
        Ok(s"OK, Wiped tables [${tables.mkString(", ")}].")
      }
    } else {
      Future.successful(NotFound("Missing key."))
    }
  }
}
