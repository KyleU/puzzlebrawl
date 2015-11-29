package controllers.admin

import java.util.UUID

import akka.pattern.ask
import akka.util.Timeout
import controllers.BaseController
import models._
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Schema
import services.scheduled.ScheduledTask
import services.supervisor.ActorSupervisor
import services.user.{ UserService, AuthenticationEnvironment }

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Random

@javax.inject.Singleton
class AdminController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment,
    scheduledTask: ScheduledTask
) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def index = withAdminSession("index") { implicit request =>
    Future.successful(Ok(views.html.admin.index()))
  }

  def enable = withSession("admin.enable") { implicit request =>
    UserService.enableAdmin(request.identity).map { response =>
      Ok(response)
    }
  }

  def status = withAdminSession("status") { implicit request =>
    (ActorSupervisor.instance ask GetSystemStatus).map {
      case x: SystemStatus => Ok(views.html.admin.activity.status(x))
    }
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

  def observeRandomBrawl() = withAdminSession("observe.random") { implicit request =>
    (ActorSupervisor.instance ask GetSystemStatus).map {
      case ss: SystemStatus => if (ss.brawls.isEmpty) {
        Ok("No brawls available.")
      } else {
        val gameId = ss.brawls(new Random().nextInt(ss.brawls.length))._1
        Ok("TODO")
      }
      case se: ServerError => Ok(s"${se.reason}: ${se.content}")
    }
  }

  def observeBrawlAsAdmin(gameId: UUID) = withAdminSession("observe.brawl") { implicit request =>
    Future.successful(Ok("TODO: " + gameId))
  }

  def observeBrawlAs(gameId: UUID, as: UUID) = withAdminSession("observe.brawl.as") { implicit request =>
    Future.successful(Ok("TODO: " + gameId + " [" + as + "]"))
  }
}
