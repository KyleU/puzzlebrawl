package json

import models.ui.MenuEntry
import upickle._
import upickle.legacy._

object MenuSerializers {
  private[this] def toJson(e: MenuEntry): Js.Obj = {
    val title = "title" -> Js.Str(e.title)
    val action = e.action.map(x => "action" -> Js.Str(x)).toSeq
    val children = e.children.map { x =>
      val vals = x.map(y => toJson(y))
      "children" -> Js.Arr(vals: _*)
    }.toSeq

    val keyVals = title +: (action ++ children)
    Js.Obj(keyVals: _*)
  }

  private[this] def fromJson(json: Js.Obj): MenuEntry = {
    val v = json.value.toMap
    val title = v("title") match {
      case x: Js.Str => x.value
    }

    val action = v.get("action").map {
      case s: Js.Str => s.value
    }

    val children = v.get("children").map {
      case a: Js.Arr => a.value.map {
        case o: Js.Obj => fromJson(o)
      }
    }

    MenuEntry(title, action = action, children = children)
  }

  implicit val menuEntryReader = Reader[MenuEntry] { case json: Js.Obj => fromJson(json) }
  implicit val menuEntryWriter = Writer[MenuEntry] { case g => toJson(g) }

}
