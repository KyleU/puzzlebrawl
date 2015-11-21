package utils.json

import models.game.Game
import models.game.board.Board
import models.game.gem.{ GemLocation, GemStream, Color, Gem }
import models.game.player.Player
import play.api.libs.json._

object GameSerializers {
  implicit val colorReads = new Reads[Color] {
    override def reads(json: JsValue) = JsSuccess(Color.fromChar(json.as[String].head))
  }
  implicit val colorWrites = new Writes[Color] {
    override def writes(color: Color) = JsString(color.charVal.toString)
  }

  implicit val gemReads = Json.reads[Gem]
  implicit val gemWrites = Json.writes[Gem]

  implicit val gemLocationReads = Json.reads[GemLocation]
  implicit val gemLocationWrites = Json.writes[GemLocation]

  implicit val spacesReads = new Reads[Array[Array[Option[Gem]]]] {
    override def reads(json: JsValue) = {
      val jsCols = json.as[Seq[JsValue]]
      val cols = jsCols.map { c =>
        c.as[Seq[JsValue]].map {
          case o: JsObject => Some(Json.fromJson[Gem](o).get)
          case _ => None
        }.toArray
      }
      val result = cols.toArray
      JsSuccess(result)
    }
  }
  implicit val spacesWrites = new Writes[Array[Array[Option[Gem]]]] {
    override def writes(spaces: Array[Array[Option[Gem]]]) = JsArray(spaces.map { col =>
      JsArray(col.map(x => x.map(x => Json.toJson(x)).getOrElse(JsNull)))
    })
  }

  implicit val boardReads = Json.reads[Board]
  implicit val boardWrites = new Writes[Board] {
    override def writes(o: Board) = {
      val json = Json.writes[Board].writes(o)
      json.as[JsObject] ++ JsObject(Seq("spaces" -> Json.toJson(o.getSpacesCopy)))
    }
  }

  implicit val gemStreamReads = Json.reads[GemStream]
  implicit val gemStreamWrites = Json.writes[GemStream]

  implicit val playerReads = Json.reads[Player]
  implicit val playerWrites = Json.writes[Player]

  implicit val gameReads = Json.reads[Game]
  implicit val gameWrites = Json.writes[Game]
}
