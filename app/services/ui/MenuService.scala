package services.ui

import models.scenario.Scenario
import models.ui.MenuEntry
import models.user.User
import play.api.libs.json._
import utils.Config

object MenuService {
  implicit val menuEntryWrites: Writes[MenuEntry] = new Writes[MenuEntry] {
    override def writes(e: MenuEntry) = {
      val title = Seq("title" -> JsString(e.title))
      val act = e.action.map(a => "action" -> JsString(a)).toSeq
      val children = e.children.map(cs => "children" -> JsArray(cs.map(menuEntryWrites.writes)))
      JsObject(title ++ act ++ children)
    }
  }

  def menuFor(identity: User) = if (identity.isAdmin) { adminMenu } else { basicMenu }

  private def act(title: String, action: String) = MenuEntry(title, action = Some(action))
  private def fld(title: String, children: Seq[MenuEntry]) = MenuEntry(title, children = Some(children))

  private[this] val commonActions = Seq(
    act("Play Now", "play"),
    fld("Scenarios", Scenario.all.map(x => act(x._1, x._2)))
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
