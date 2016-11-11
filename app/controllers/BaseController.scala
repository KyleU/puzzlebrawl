package controllers

import java.util.UUID

import models.history.RequestLog
import services.history.RequestHistoryService
import play.api.i18n.I18nSupport
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.user.{ UserPreferences, Role, User }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{ AnyContent, RequestHeader, Result }
import utils.{ ApplicationContext, DateUtils, Logging }
import utils.metrics.Instrumented

import scala.concurrent.Future

abstract class BaseController() extends Silhouette[User, CookieAuthenticator] with I18nSupport with Instrumented with Logging {
  def ctx: ApplicationContext

  override def messagesApi = ctx.messagesApi
  override def env = ctx.env

  def withAdminSession(action: String)(block: (SecuredRequest[AnyContent]) => Future[Result]) = SecuredAction.async { implicit request =>
    val startTime = System.nanoTime
    metrics.timer(action).timeFuture {
      if (request.identity.roles.contains(Role.Admin)) {
        block(request).map { r =>
          val duration = ((System.nanoTime - startTime) / 1000000).toInt
          logRequest(request, request.identity.id, request.authenticator.loginInfo, duration, r.header.status)
          r
        }
      } else {
        Future.successful(NotFound("404 Not Found"))
      }
    }
  }

  def withSession(action: String)(block: (SecuredRequest[AnyContent]) => Future[Result]) = UserAwareAction.async { implicit request =>
    val startTime = System.nanoTime
    metrics.timer(action).timeFuture {
      request.identity match {
        case Some(user) =>
          val secured = SecuredRequest(user, request.authenticator.getOrElse(throw new IllegalStateException()), request)
          block(secured).map { r =>
            val duration = ((System.nanoTime - startTime) / 1000000).toInt
            logRequest(secured, secured.identity.id, secured.authenticator.loginInfo, duration, r.header.status)
            r
          }
        case None =>
          val user = User(
            id = UUID.randomUUID(),
            username = None,
            preferences = UserPreferences(),
            profiles = Nil,
            created = DateUtils.now
          )

          for {
            user <- ctx.env.userService.save(user)
            authenticator <- env.authenticatorService.create(LoginInfo("anonymous", user.id.toString))
            value <- env.authenticatorService.init(authenticator)
            result <- block(SecuredRequest(user, authenticator, request))
            authedResponse <- env.authenticatorService.embed(value, result)
          } yield {
            env.eventBus.publish(SignUpEvent(user, request, request2Messages))
            env.eventBus.publish(LoginEvent(user, request, request2Messages))
            val duration = ((System.nanoTime - startTime) / 1000000).toInt
            logRequest(request, user.id, authenticator.loginInfo, duration, authedResponse.header.status)
            authedResponse
          }
      }
    }
  }

  private[this] def logRequest(request: RequestHeader, userId: UUID, loginInfo: LoginInfo, duration: Int, status: Int) = {
    val log = RequestLog(request, userId, loginInfo, duration, status)
    RequestHistoryService.insert(log)
  }
}
