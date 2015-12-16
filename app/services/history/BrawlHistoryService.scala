package services.history

import java.util.UUID

import com.github.mauricio.async.db.Connection
import models.queries.history.BrawlHistoryQueries
import models.history.BrawlHistory
import org.joda.time.LocalDateTime
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database

import scala.concurrent.Future

object BrawlHistoryService {
  def getBrawlHistory(id: UUID) = Database.query(BrawlHistoryQueries.getById(id))

  def getAll = Database.query(BrawlHistoryQueries.search("", "created", None))

  def searchBrawls(q: String, orderBy: String, page: Int) = for {
    count <- Database.query(BrawlHistoryQueries.searchCount(q))
    list <- Database.query(BrawlHistoryQueries.search(q, getOrderClause(orderBy), Some(page)))
  } yield count -> list

  def getCountByUser(id: UUID) = Database.query(BrawlHistoryQueries.getBrawlHistoryCountForUser(id))

  def insert(bh: BrawlHistory) = Database.execute(BrawlHistoryQueries.insert(bh)).map(ok => true)

  def setCounts(id: UUID, gems: Seq[Int], moves: Seq[Int]) = Database.execute(BrawlHistoryQueries.SetCounts(id, gems, moves)).map(_ == 1)
  def setFirstMove(id: UUID, firstMove: LocalDateTime) = Database.execute(BrawlHistoryQueries.SetFirstMove(id, firstMove)).map(_ == 1)
  def setCompleted(id: UUID, completed: LocalDateTime, status: String) = Database.execute(BrawlHistoryQueries.SetCompleted(id, completed, status)).map(_ == 1)

  def removeBrawlHistory(id: UUID, conn: Option[Connection]) = for {
    success <- Database.execute(BrawlHistoryQueries.removeById(Seq(id)), conn).map(_ == 1)
  } yield (id, success)

  def removeBrawlHistoriesByUser(userId: UUID) = {
    Database.query(BrawlHistoryQueries.GetBrawlHistoryIdsForUser(userId)).flatMap { brawlIds =>
      Future.sequence(brawlIds.map(id => removeBrawlHistory(id, None)))
    }
  }

  private[this] def getOrderClause(orderBy: String) = orderBy match {
    case "game-id" => "id"
    case "created" => "created desc"
    case "completed" => "completed desc"
    case x => x
  }
}
