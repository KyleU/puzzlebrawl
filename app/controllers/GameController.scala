package controllers

import play.api.i18n.MessagesApi
import services.user.AuthenticationEnvironment

import scala.concurrent.Future

@javax.inject.Singleton
class GameController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def play() = withSession("play") { implicit request =>
    Future.successful(Ok(views.html.game.gameplay(request.identity)))
  }
}
