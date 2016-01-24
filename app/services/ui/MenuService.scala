package services.ui

import models.scenario.Scenario
import models.ui.MenuEntry
import models.user.User
import play.api.libs.json._
import utils.Config

object MenuService {
  def menuFor(identity: User) = if (identity.isAdmin) { adminMenu } else { basicMenu }

  private def act(title: String, action: String) = MenuEntry(title, action = Some(action))
  private def fld(title: String, children: Seq[MenuEntry]) = MenuEntry(title, children = Some(children))

  private[this] val commonActions = Seq(
    act("Play Now", "play"),
    fld("Scenarios", Scenario.all.map(x => act(x._2, x._1)))
  )

  private[this] val settingsActions = Seq(
    act("Preferences", "prefs"),
    act("Statistics", "stats"),
    act("Feedback", "feedback")
  )

  private[this] val adminActions = Seq(
    fld("Tests", Seq.empty),
    act("Admin", "admin")
  )

  private[this] val basicMenu = fld(Config.projectName, commonActions ++ settingsActions)
  private[this] val adminMenu = fld(Config.projectName + " Admin", commonActions ++ adminActions ++ settingsActions)
}
