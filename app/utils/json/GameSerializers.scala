package utils.json

import models.game.board.Board
import models.game.gem.{ Color, Gem }
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

  implicit val boardReads = Json.reads[Board]
  implicit val boardWrites = Json.writes[Board]
}
