package services.connection

import models._
import models.queries.auth.UserQueries
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database
import services.user.UserService
import utils.cache.UserCache
import utils.metrics.InstrumentedActor

trait ConnectionServicePreferenceHelper extends InstrumentedActor { this: ConnectionService =>
  protected[this] def handleSetPreference(sp: SetPreference): Unit = {
    if (sp.name == "username") {
      UserService.isUsernameInUse(sp.value).foreach { inUse =>
        if (inUse) {
          log.info(s"Cannot change name for user [${user.id}}] from [${user.username}] to already-in-use [${sp.value}].")
          out ! PreferenceChanged(sp.name, sp.value, "already-claimed")
        } else {
          currentUsername match {
            case Some(n) => log.info(s"Changing name for user [${user.id}}] from [$n] to [${sp.value}].")
            case None => log.info(s"Setting initial name for user [${user.id}}] to [${sp.value}].")
          }
          val newUser = user.copy(username = Some(sp.value))
          UserService.save(newUser, update = true).map { u =>
            currentUsername = u.username
            out ! PreferenceChanged(sp.name, sp.value, "ok")
          }
        }
      }
    } else {
      val newPrefs = sp.name match {
        case "theme" => userPreferences.copy(theme = sp.value)
        case x => log.errorThenThrow(s"Unhandled preference [$x] for user [$user.id] with value [${sp.value}].")
      }
      if (newPrefs != userPreferences) {
        Database.execute(UserQueries.SetPreferences(user.id, newPrefs)).map { x =>
          userPreferences = newPrefs
          UserCache.removeUser(user.id)
        }
      }
    }
  }
}
