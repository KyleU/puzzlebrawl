package models.history

import java.util.UUID

import org.joda.time.LocalDateTime
import utils.DateUtils

case class BrawlHistory(
    id: UUID,
    seed: Int,
    scenario: String,
    status: String,
    players: Seq[UUID],
    gems: Seq[Int],
    moves: Seq[Int],
    started: LocalDateTime,
    firstMove: Option[LocalDateTime],
    completed: Option[LocalDateTime],
    logged: Option[LocalDateTime]) {
  lazy val duration = {
    val createdMillis = DateUtils.toMillis(started)
    val completedMillis = completed match {
      case Some(t) => DateUtils.toMillis(t)
      case None => DateUtils.nowMillis
    }
    completedMillis - createdMillis
  }
  val isWin = status == "win"
  val isCompleted = completed.isDefined
  val isLogged = logged.isDefined
}
