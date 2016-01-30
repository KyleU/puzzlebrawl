package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{ JsObject, JsString }
import play.api.mvc.Action
import services.audit.AnalyticsService
import utils.ApplicationContext

import scala.concurrent.Future

@javax.inject.Singleton
class HomeController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  def fakeIndex() = withSession("index") { implicit request =>
    Future.successful(Ok("puzzlebrawl.com"))
  }

  def play() = withSession("play") { implicit request =>
    Future.successful(Ok(views.html.brawl.brawl(request.identity, debug = ctx.config.debug)))
  }

  def untrail(path: String) = Action.async {
    Future.successful(MovedPermanently(s"/$path"))
  }

  def externalLink(url: String) = withSession("external.link") { implicit request =>
    Future.successful(Redirect(if (url.startsWith("http")) { url } else { "http://" + url }))
  }

  def error() = withSession("error") { implicit request =>
    request.body.asJson match {
      case Some(json) =>
        val sourceAddress = request.remoteAddress
        AnalyticsService.error(request.identity.id, sourceAddress, json).map { result =>
          val msg = s"Error received from user [${request.identity.id}]."
          ctx.notificationService.alert(msg, "#production-errors")
          Ok(JsObject(Map("result" -> JsString("All good, thanks for the report!"))))
        }
      case None =>
        log.warn("Cannot parse error report with content [" + request.body.asText.getOrElse("???") + "].")
        Future.successful(Ok(JsObject(Map("result" -> JsString("Invalid content.")))))
    }
  }
}
