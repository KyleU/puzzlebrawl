package controllers.admin

import akka.util.Timeout
import controllers.BaseController
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.sandbox._
import services.scheduled.ScheduledTask
import utils.{ ApplicationContext, DateUtils }

import scala.concurrent.Future
import scala.concurrent.duration._

object SandboxController {
  val sandboxes = Seq(
    Scratchpad,
    RunScheduledTask,
    SendErrorEmail,
    BackfillMetrics,
    RemoveUsers,
    HtmlSandbox
  )
}

@javax.inject.Singleton
class SandboxController @javax.inject.Inject() (override val ctx: ApplicationContext, scheduledTask: ScheduledTask) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def defaultSandbox() = sandbox("list")

  RunScheduledTask.scheduledTask = Some(scheduledTask)

  def sandbox(key: String) = withAdminSession(key) { implicit request =>
    implicit val identity = request.identity
    val sandbox = SandboxController.sandboxes.find(_.id == key).getOrElse(throw new IllegalStateException())
    if (sandbox == HtmlSandbox) {
      Future.successful(Ok(views.html.admin.test.sandbox(java.util.UUID.randomUUID())))
    } else {
      sandbox.run(ctx).map { result =>
        Ok(result)
      }
    }
  }

  private[this] def runErrorMail() = Future.successful(
    views.html.email.severeErrorHtml(
      "Error Message",
      "Test Context",
      Some(new Exception("Text Exception")),
      None,
      DateUtils.now
    )
  )
}
