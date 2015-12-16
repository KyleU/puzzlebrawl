package controllers.admin

import java.util.UUID

import akka.pattern.ask
import akka.util.Timeout
import controllers.BaseController
import models._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.supervisor.ActorSupervisor
import utils.ApplicationContext

import scala.concurrent.duration._

@javax.inject.Singleton
class TraceController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def traceConnection(connectionId: UUID) = withAdminSession("connection") { implicit request =>
    (ActorSupervisor.instance ask ConnectionTrace(connectionId)).map {
      case tr: TraceResponse => Ok(views.html.admin.activity.trace("Connection", tr))
      case se: ServerError => Ok(s"${se.reason}: ${se.content}")
    }
  }

  def traceClient(connectionId: UUID) = withAdminSession("client") { implicit request =>
    (ActorSupervisor.instance ask ClientTrace(connectionId)).map {
      case tr: TraceResponse => Ok(views.html.admin.activity.trace("Client", tr))
      case se: ServerError => Ok(s"${se.reason}: ${se.content}")
    }
  }

  def traceBrawl(brawlId: UUID) = withAdminSession("brawl") { implicit request =>
    implicit val identity = request.identity
    (ActorSupervisor.instance ask BrawlTrace(brawlId)).map {
      case tr: TraceResponse =>
        val buttons = Seq("Observe As Admin" -> controllers.admin.routes.AdminController.observeBrawlAsAdmin(brawlId).url)
        Ok(views.html.admin.activity.trace("Brawl", tr, buttons))
      case se: ServerError => Ok(s"${se.reason}: ${se.content}")
    }
  }
}
