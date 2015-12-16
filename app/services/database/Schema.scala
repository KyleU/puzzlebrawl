package services.database

import models.database.Statement
import models.ddl.DdlQueries
import models.ddl._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.Logging

import scala.concurrent.Future

object Schema extends Logging {
  val tables = Seq(
    CreateUsersTable,

    CreateUserProfilesTable,
    CreatePasswordInfoTable,
    CreateOAuth1InfoTable,
    CreateOAuth2InfoTable,
    CreateOpenIdInfoTable,
    CreateSessionInfoTable,

    CreateBrawlsTable,
    CreateRequestsTable,
    CreateClientTraceTable,

    CreateUserFeedbackTable,
    CreateUserFeedbackNotesTable,

    CreateDailyMetricsTable,

    CreateAdHocQueriesTable
  )

  def update() = {
    val tableFuture = tables.foldLeft(Future.successful(Unit)) { (f, t) =>
      f.flatMap { u =>
        Database.query(DdlQueries.DoesTableExist(t.tableName)).flatMap { exists =>
          if (exists) {
            Future.successful(Unit)
          } else {
            log.info(s"Creating missing table [${t.tableName}].")
            val name = s"CreateTable-${t.tableName}"
            Database.raw(name, t.sql).map(x => Unit)
          }
        }
      }
    }

    tableFuture.flatMap { ok =>
      createUser(Database.query(DdlQueries.DoesTestUserExist), DdlQueries.InsertTestUser)
    }
  }

  def wipe() = {
    log.warn("Wiping database schema.")
    val tableNames = tables.reverse.map(_.tableName)
    Database.execute(DdlQueries.TruncateTables(tableNames)).map(x => tableNames)
  }

  private[this] def createUser(q: Future[Boolean], insert: Statement) = q.flatMap { exists =>
    if (exists) {
      Future.successful(Unit)
    } else {
      log.info(s"Creating user [${insert.getClass.getSimpleName}].")
      Database.execute(insert).map(x => x == 1)
    }
  }
}
