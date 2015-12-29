package controllers

import play.api.mvc.Action
import utils.ApplicationContext

import scala.concurrent.Future

@javax.inject.Singleton
class HomeController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  def fakeIndex() = withSession("index") { implicit request =>
    Future.successful(Ok("puzzlebrawl.com"))
  }

  def index() = withSession("index") { implicit request =>
    Future.successful(Ok(views.html.index(request.identity)))
  }

  def untrail(path: String) = Action.async {
    Future.successful(MovedPermanently(s"/$path"))
  }

  def externalLink(url: String) = withSession("external.link") { implicit request =>
    Future.successful(Redirect(if (url.startsWith("http")) { url } else { "http://" + url }))
  }

  def ping(timestamp: Long) = Action.async { implicit request =>
    Future.successful(Ok(timestamp.toString))
  }
}
