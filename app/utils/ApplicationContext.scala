package utils

import akka.actor.Props
import play.api.Play._
import play.api.http.HttpRequestHandler
import play.api.i18n.MessagesApi
import play.api.inject.ApplicationLifecycle
import play.api.libs.concurrent.Akka
import play.api.mvc.{ Action, RequestHeader, Results }
import play.api.routing.Router
import services.supervisor.ActorSupervisor
import services.user.AuthenticationEnvironment

object ApplicationContext {
  var initialized = false

  class SimpleHttpRequestHandler @javax.inject.Inject() (router: Router) extends HttpRequestHandler {
    def handlerForRequest(request: RequestHeader) = {
      router.routes.lift(request) match {
        case Some(handler) => (request, handler)
        case None => (request, Action(Results.NotFound))
      }
    }
  }
}

@javax.inject.Singleton
class ApplicationContext @javax.inject.Inject() (
    val env: AuthenticationEnvironment,
    val messagesApi: MessagesApi,
    val lifecycle: ApplicationLifecycle
) extends ApplicationContextHelper with Logging {
  start()
}