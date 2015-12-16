package controllers.admin

import java.util.UUID

import controllers.BaseController
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.history.BrawlHistoryService
import services.user.AuthenticationEnvironment

import scala.concurrent.Future
@javax.inject.Singleton
class BrawlHistoryController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment
) extends BaseController {
  def brawlList(q: String, sortBy: String, page: Int) = withAdminSession("list") { implicit request =>
    implicit val identity = request.identity
    BrawlHistoryService.searchBrawls(q, sortBy, page).map { brawls =>
      Ok(views.html.admin.brawl.brawlList(q, sortBy, brawls._1, page, brawls._2))
    }
  }

  def brawlDetail(id: UUID) = withAdminSession("detail") { implicit request =>
    implicit val identity = request.identity
    BrawlHistoryService.getBrawlHistory(id).flatMap {
      case Some(brawl) => Future.successful(Ok(views.html.admin.brawl.brawlDetail(brawl)))
      case None => Future.successful(NotFound(s"Brawl [$id] not found."))
    }
  }

  def removeBrawl(id: UUID) = withAdminSession("remove") { implicit request =>
    BrawlHistoryService.removeBrawlHistory(id, None).map { ok =>
      val msg = s"Brawl [$id] removed."
      Redirect(controllers.admin.routes.BrawlHistoryController.brawlList("")).flashing("success" -> msg)
    }
  }
}
