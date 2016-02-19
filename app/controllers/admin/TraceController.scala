package controllers.admin

import java.util.UUID

import akka.pattern.ask
import akka.util.Timeout
import controllers.BaseController
import models._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.ApplicationContext

import scala.concurrent.duration._

@javax.inject.Singleton
class TraceController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def traceConnection(connectionId: UUID) = withAdminSession("connection") { implicit request =>
    (ctx.supervisor ask SendConnectionTrace(connectionId)).map {
      case tr: ConnectionTraceResponse => Ok(views.html.admin.activity.connectionTrace(tr))
      case se: ServerError => Ok(s"${se.reason}: ${se.content}")
    }
  }

  def traceClient(connectionId: UUID) = withAdminSession("client") { implicit request =>
    (ctx.supervisor ask SendClientTrace(connectionId)).map {
      case tr: ClientTraceResponse => Ok(views.html.admin.activity.clientTrace(tr))
      case se: ServerError => Ok(s"${se.reason}: ${se.content}")
    }
  }

  def traceBrawl(brawlId: UUID) = withAdminSession("brawl") { implicit request =>
    implicit val identity = request.identity
    (ctx.supervisor ask SendBrawlTrace(brawlId)).map {
      case tr: BrawlTraceResponse => Ok(views.html.admin.activity.brawlTrace(tr))
      case se: ServerError => Ok(s"${se.reason}: ${se.content}")
    }
  }
}
