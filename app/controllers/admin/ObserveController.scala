package controllers.admin

import java.util.UUID

import akka.pattern.ask
import akka.util.Timeout
import controllers.BaseController
import models._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.scheduled.ScheduledTask
import utils.ApplicationContext

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Random

@javax.inject.Singleton
class ObserveController @javax.inject.Inject()(override val ctx: ApplicationContext, scheduledTask: ScheduledTask) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def observeRandomBrawl() = withAdminSession("observe.random") { implicit request =>
    (ctx.supervisor ask GetSystemStatus).map {
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
    Future.successful(Redirect(controllers.routes.GameController.play() + "#observe-" + gameId))
  }

  def observeBrawlAs(gameId: UUID, as: UUID) = withAdminSession("observe.brawl.as") { implicit request =>
    Future.successful(Redirect(controllers.routes.GameController.play() + "#observe-" + gameId + "(" + as + ")"))
  }
}
