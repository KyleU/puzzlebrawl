package utils.web

import akka.actor.ActorSystem
import play.api.Play.current
import play.api.mvc.{ RequestHeader, Results, WithFilters }
import play.api.{ Application, GlobalSettings, Mode }
import play.filters.gzip.GzipFilter
import services.scheduled.ScheduledTask
import utils.Logging

import scala.concurrent.Future

object PlayGlobalSettings extends WithFilters(PlayLoggingFilter, new GzipFilter()) with GlobalSettings with Logging {
  override def onError(request: RequestHeader, ex: Throwable) = if (current.mode == Mode.Dev) {
    super.onError(request, ex)
  } else {
    Future.successful(
      Results.InternalServerError(views.html.error.serverError(request.path, Some(ex))(request.session, request.flash))
    )
  }

  override def onHandlerNotFound(request: RequestHeader) = Future.successful(
    Results.NotFound(views.html.error.notFound(request.path)(request.session, request.flash))
  )

  override def onBadRequest(request: RequestHeader, error: String) = Future.successful(
    Results.BadRequest(views.html.error.badRequest(request.path, error)(request.session, request.flash))
  )

  override def onRouteRequest(request: RequestHeader) = {
    if (!Option(request.path).exists(_.startsWith("/assets"))) {
      log.info(s"Request from [${request.remoteAddress}]: ${request.toString()}")
    }
    super.onRouteRequest(request)
  }
}
