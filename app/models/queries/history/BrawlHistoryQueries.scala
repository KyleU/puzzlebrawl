package models.queries.history

import java.util.UUID

import models.queries.BaseQueries
import models.database.{ Query, Row, Statement }
import models.history.BrawlHistory
import org.joda.time.{ LocalDate, LocalDateTime }

object BrawlHistoryQueries extends BaseQueries[BrawlHistory] {
  override protected val tableName = "brawls"
  override protected val columns = Seq(
    "id", "seed", "scenario", "status", "players", "normal_gems", "timer_gems", "moves", "created", "first_move", "completed", "logged"
  )
  override protected val searchColumns = Seq("id::text", "seed::text", "scenario", "status", "players::text")

  def getById(id: UUID) = getBySingleId(id)
  val insert = Insert
  def searchCount(q: String, groupBy: Option[String] = None) = new SearchCount(q, groupBy)
  val search = Search
  val removeById = RemoveById

  final case class SetCounts(id: UUID, gems: Seq[Int], moves: Seq[Int]) extends Statement {
    override val sql = updateSql(Seq("gems", "moves"))
    override val values = Seq[Any](gems, moves, id)
  }

  final case class SetFirstMove(id: UUID, firstMove: LocalDateTime) extends Statement {
    override def sql = updateSql(Seq("first_move"))
    override def values = Seq(firstMove, id)
  }

  final case class SetCompleted(id: UUID, completed: LocalDateTime, status: String) extends Statement {
    override def sql = updateSql(Seq("completed", "status"))
    override def values = Seq(completed, status, id)
  }

  final case class GetBrawlHistoriesByDayAndStatus(d: LocalDate, status: String) extends Query[Seq[BrawlHistory]] {
    override def sql = getSql(whereClause = Some("completed >= ? and completed < ? and status = ?"), orderBy = Some("completed"))
    override def values = Seq(d, d.plusDays(1), status)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  final case class GetBrawlHistoryIdsForUser(userId: UUID) extends Query[List[UUID]] {
    override val sql = s"select id from $tableName where player = ?"
    override val values = Seq(userId)
    override def reduce(rows: Iterator[Row]) = rows.map(_.as[UUID]("id")).toList
  }

  def getBrawlHistoryCountForUser(userId: UUID) = new Count(s"select count(*) as c from $tableName where player = ?", Seq(userId))

  override protected def fromRow(row: Row) = {
    val id = row.as[UUID]("id")
    val seed = row.as[Int]("seed")
    val scenario = row.as[String]("scenario")
    val status = row.as[String]("status")
    val players = row.as[Seq[UUID]]("players")
    val normalGems = row.as[Seq[Int]]("normal_gems")
    val timerGems = row.as[Seq[Int]]("timer_gems")
    val moves = row.as[Seq[Int]]("moves")
    val created = row.as[LocalDateTime]("created")
    val firstMove = row.asOpt[LocalDateTime]("first_move")
    val completed = row.asOpt[LocalDateTime]("completed")
    val logged = row.asOpt[LocalDateTime]("logged")
    BrawlHistory(id, seed, scenario, status, players, normalGems, timerGems, moves, created, firstMove, completed, logged)
  }

  override protected def toDataSeq(bh: BrawlHistory) = Seq[Any](
    bh.id, bh.seed, bh.scenario, bh.status, bh.players,
    bh.normalGems, bh.timerGems, bh.moves, bh.started, bh.firstMove, bh.completed, bh.logged
  )
}
