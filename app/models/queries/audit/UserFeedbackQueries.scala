package models.queries.audit

import java.util.UUID

import models.audit.UserFeedback
import models.queries.BaseQueries
import models.database.{ Query, Row }
import org.joda.time.LocalDateTime

object UserFeedbackQueries extends BaseQueries[UserFeedback] {
  override protected val tableName = "user_feedback"
  override protected val columns = Seq("id", "user_id", "brawl_id", "context", "contact", "content", "occurred")
  override protected val searchColumns = Seq("id::text", "user_id::text", "feedback")

  val insert = Insert
  def searchCount(q: String, groupBy: Option[String] = None) = new SearchCount(q, groupBy)
  def getById(id: UUID) = getBySingleId(id)
  val search = Search
  val remove = RemoveById

  case class GetUserFeedbackByUser(id: UUID, sortBy: String) extends Query[List[UserFeedback]] {
    override val sql = getSql(whereClause = Some("user_id = ?"), orderBy = Some("?"))
    override val values = Seq(id, sortBy)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  override protected def fromRow(row: Row) = {
    val id = row.as[UUID]("id")
    val userId = row.as[UUID]("user_id")
    val brawlId = row.asOpt[UUID]("brawl_id")
    val context = row.as[String]("context")
    val contact = row.asOpt[String]("contact")
    val content = row.as[String]("feedback")
    val occurred = row.as[LocalDateTime]("occurred")
    UserFeedback(id, userId, brawlId, context, contact, content, occurred)
  }

  override protected def toDataSeq(f: UserFeedback) = Seq[Any](f.id, f.userId, f.brawlId, f.context, f.contact, f.content, f.occurred)
}
