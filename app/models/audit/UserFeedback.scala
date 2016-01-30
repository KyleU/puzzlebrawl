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
}

case class UserFeedback(
    id: UUID,
    userId: UUID,
    username: Option[String],
    brawlId: Option[UUID],
    context: String,
    contact: Option[String],
    content: String,
    occurred: LocalDateTime) {
}
