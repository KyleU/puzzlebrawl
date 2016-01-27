package models.audit

import java.util.UUID

import org.joda.time.LocalDateTime

object UserFeedback {
  case class FeedbackNote(
    id: UUID,
    feedbackId: UUID,
    authorId: UUID,
    content: String,
    occurred: LocalDateTime)

  /*
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
   */
}

case class UserFeedback(
    id: UUID,
    userId: UUID,
    brawlId: Option[UUID],
    context: String,
    contact: Option[String],
    content: String,
    occurred: LocalDateTime) {
}
