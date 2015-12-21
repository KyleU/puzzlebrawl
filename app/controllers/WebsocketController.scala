package controllers

import akka.actor.ActorRef
import models.{ RequestMessage, ResponseMessage }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{ AnyContentAsEmpty, Request, WebSocket }
import services.connection.ConnectionService
import utils.ApplicationContext
import utils.web.MessageFrameFormatter

import scala.concurrent.Future

@javax.inject.Singleton
class WebsocketController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  import play.api.Play.current
  val mff = new MessageFrameFormatter(ctx.config.debug)

  import mff.{ requestFormatter, responseFormatter }

  def connect() = WebSocket.tryAcceptWithActor[RequestMessage, ResponseMessage] { request =>
    implicit val req = Request(request, AnyContentAsEmpty)
    SecuredRequestHandler { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(r, Some(user)) => Right(ConnectionService.props(None, ctx.supervisor, user, _: ActorRef))
      case HandlerResult(r, None) => Left(r)
    }
  }
}
