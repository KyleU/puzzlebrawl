package controllers

import utils.ApplicationContext

import scala.concurrent.Future

@javax.inject.Singleton
class BrawlController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  def play() = withSession("play") { implicit request =>
    Future.successful(Ok(views.html.brawl.brawl(request.identity, debug = ctx.config.debug)))
  }
}
