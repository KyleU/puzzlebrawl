package controllers

import utils.ApplicationContext

import scala.concurrent.Future

@javax.inject.Singleton
class GameController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  def play() = withSession("play") { implicit request =>
    Future.successful(Ok(views.html.game.gameplay(request.identity, debug = true)))
  }
}
