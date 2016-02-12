package services.user

import java.util.UUID

import com.github.mauricio.async.db.Connection
import com.mohiva.play.silhouette.api.AuthInfo
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import models.queries.auth._
import models.user.{ Role, User }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database
import services.history.RequestHistoryService
import utils.Logging
import utils.cache.UserCache

import scala.concurrent.Future

object UserService extends Logging {
  def create[A <: AuthInfo](currentUser: User, profile: CommonSocialProfile): Future[User] = {
    log.info(s"Saving profile [$profile].")
    UserSearchService.retrieve(profile.loginInfo).flatMap {
      case Some(existingUser) => if (existingUser.id == currentUser.id) {
        val profiles = existingUser.profiles.filterNot(_.providerID == profile.loginInfo.providerID) :+ profile.loginInfo
        val u = existingUser.copy(profiles = profiles)
        save(u, update = true)
      } else {
        Future.successful(existingUser)
      }
      case None => Database.execute(ProfileQueries.insert(profile)).flatMap { x =>
        val u = currentUser.copy(
          profiles = currentUser.profiles.filterNot(_.providerID == profile.loginInfo.providerID) :+ profile.loginInfo
        )
        save(u, update = true)
      }
    }
  }

  def save(user: User, update: Boolean = false): Future[User] = {
    log.info(s"${if (update) { "Updating" } else { "Creating" }} user [$user].")
    val statement = if (update) {
      UserQueries.UpdateUser(user)
    } else {
      UserQueries.insert(user)
    }
    Database.execute(statement).map(i => UserCache.cacheUser(user))
  }

  def isUsernameInUse(name: String) = Database.query(UserQueries.IsUsernameInUse(name))

  def remove(userId: UUID) = {
    val startTime = System.nanoTime
    Database.transaction { conn =>
      for {
        requests <- RequestHistoryService.removeRequestsByUser(userId, Some(conn))
        profiles <- removeProfiles(userId, Some(conn)).map(_.length)
        users <- Database.execute(UserQueries.removeById(Seq(userId)), Some(conn))
      } yield {
        UserCache.removeUser(userId)
        val timing = ((System.nanoTime - startTime) / 1000000).toInt
        Map("users" -> users, "profiles" -> profiles, "requests" -> requests, "timing" -> timing)
      }
    }
  }

  def enableAdmin(user: User) = Database.query(UserQueries.CountAdmins).flatMap { adminCount =>
    if (adminCount == 0) {
      Database.execute(UserQueries.AddRole(user.id, Role.Admin)).map { x =>
        UserCache.removeUser(user.id)
        "OK"
      }
    } else {
      Future.successful(s"Forbidden. $adminCount admins already exist.")
    }
  }

  private[this] def removeProfiles(userId: UUID, conn: Option[Connection]) = Database.query(ProfileQueries.FindProfilesByUser(userId)).flatMap { profiles =>
    Future.sequence(profiles.map { profile =>
      (profile.loginInfo.providerID match {
        case "credentials" => Database.execute(PasswordInfoQueries.removeById(Seq(profile.loginInfo.providerID, profile.loginInfo.providerKey)), conn)
        case "facebook" => Database.execute(OAuth2InfoQueries.removeById(Seq(profile.loginInfo.providerID, profile.loginInfo.providerKey)), conn)
        case "google" => Database.execute(OAuth2InfoQueries.removeById(Seq(profile.loginInfo.providerID, profile.loginInfo.providerKey)), conn)
        case "twitter" => Database.execute(OAuth1InfoQueries.removeById(Seq(profile.loginInfo.providerID, profile.loginInfo.providerKey)), conn)
        case p => throw new IllegalArgumentException(s"Unknown provider [$p].")
      }).flatMap { infoCount =>
        Database.execute(ProfileQueries.remove(Seq(profile.loginInfo.providerID, profile.loginInfo.providerKey)), conn).map(i => profile)
      }
    })
  }
}
