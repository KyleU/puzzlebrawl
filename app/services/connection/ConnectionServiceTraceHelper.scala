package services.connection

import java.util.UUID

import models._
import models.audit.{ AnalyticsEvent, UserFeedback }
import models.queries.audit.UserFeedbackQueries
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{ JsObject, Json }
import services.audit.AnalyticsService
import services.database.Database
import utils.DateUtils
import utils.metrics.InstrumentedActor

trait ConnectionServiceTraceHelper extends InstrumentedActor { this: ConnectionService =>
  protected[this] def handleConnectionTrace() {
    val ret = TraceResponse(id, List(
      "userId" -> user.id,
      "name" -> user.username.getOrElse("Guest")
    ))
    sender() ! ret
  }

  protected[this] def handleClientTrace() {
    pendingDebugChannel = Some(sender())
    out ! SendTrace
  }

  protected[this] def handleFeedbackResponse(contact: String, feedback: String) = {
    val obj = UserFeedback(
      id = UUID.randomUUID,
      userId = user.id,
      username = user.username,
      brawlId = activeBrawlId,
      context = "Normal",
      contact = if (contact.isEmpty) { None } else { Some(contact) },
      content = feedback,
      occurred = DateUtils.now
    )

    val f = Database.execute(UserFeedbackQueries.insert(obj))
    f.onSuccess {
      case x => log.info(s"Feedback submitted from user [${user.id} (${user.username.getOrElse("n/a")})]:\n$feedback")
    }
    f.onFailure {
      case x => log.warn(s"Unable to save feedback from user [${user.id}].", x)
    }
  }

  protected[this] def handleDebugInfo(data: String) = pendingDebugChannel match {
    case Some(dc) =>
      val json = Json.parse(data).as[JsObject]
      AnalyticsService.trace(user.id, sourceAddress, json)
      dc ! TraceResponse(id, json.fields)
    case None => activeBrawl match {
      case Some(brawl) => brawl.forward(DebugInfo(data))
      case None => log.warn(s"Received unsolicited DebugInfo [$data] from [$id] with no active brawl.")
    }
  }
}
