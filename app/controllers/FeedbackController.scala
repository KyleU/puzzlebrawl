package controllers

import java.util.UUID

import models.audit.UserFeedback
import models.queries.audit.UserFeedbackQueries
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database
import services.email.EmailService
import utils.{ ApplicationContext, DateUtils }

import scala.concurrent.Future

@javax.inject.Singleton
class FeedbackController @javax.inject.Inject() (override val ctx: ApplicationContext, emailService: EmailService) extends BaseController {
  def feedbackForm = withSession("form") { implicit request =>
    Future.successful(Ok(views.html.feedback(request.identity)))
  }

  def submitFeedback = withSession("submit") { implicit request =>
    request.body.asFormUrlEncoded match {
      case Some(form) => form.get("feedback") match {
        case Some(feedback) =>
          val brawlId = form.get("brawl").flatMap(_.headOption.map(UUID.fromString))
          val context = form.get("context").flatMap(_.headOption).getOrElse("unknown")
          val contact = form.get("contact").flatMap(_.headOption)
          val obj = UserFeedback(
            id = UUID.randomUUID,
            userId = request.identity.id,
            brawlId = brawlId,
            context = context,
            contact = contact,
            content = feedback.mkString("\n\n"),
            occurred = DateUtils.now
          )

          emailService.feedbackSubmitted(obj, request.identity)

          Database.execute(UserFeedbackQueries.insert(obj)).map { x =>
            Ok("Your feedback has been submitted. Thanks!")
          }
        case None => Future.successful(Redirect(routes.FeedbackController.feedbackForm()).flashing("error" -> "Please include some feedback."))
      }
      case None => Future.successful(Redirect(routes.FeedbackController.feedbackForm()).flashing("error" -> "Please include some feedback."))
    }
  }
}
