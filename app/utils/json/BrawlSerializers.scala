package utils.json

import models.board.Board
import models.brawl.Brawl
import models.gem.{ GemLocation, GemStream, Color, Gem }
import models.player.Player
import play.api.libs.json._

object BrawlSerializers {
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

  private[this] val internalBoardReader = Json.reads[Board]
  implicit val boardReads = new Reads[Board] {
    override def reads(json: JsValue) = internalBoardReader.reads(json) match {
      case JsSuccess(board, _) => Json.fromJson[Array[Array[Option[Gem]]]](json) match {
        case JsSuccess(spaces, _) =>
          spaces.indices.foreach { x =>
            spaces.head.indices.foreach { y =>
              if(spaces(x)(y).isDefined) {
                board.set(x, y, spaces(x)(y))
              }
            }
          }
          JsSuccess(board)
        case se: JsError => se
      }
      case e: JsError => e
    }
  }
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

  implicit val brawlReads = Json.reads[Brawl]
  implicit val brawlWrites = Json.writes[Brawl]
}
