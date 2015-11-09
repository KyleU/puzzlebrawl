package models.audit

import org.joda.time.LocalDateTime
import play.twirl.api.Html

case class ServerLog(
  level: LogLevel,
  line: Int,
  logger: String,
  thread: String,
  message: String,
  occurred: LocalDateTime
)
