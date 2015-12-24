package utils.json

import models._
import play.api.libs.json._

object RequestMessageSerializers {
  private[this] val malformedRequestReads = Json.reads[MalformedRequest]
  private[this] val pingReads = Json.reads[Ping]
  // case object [GetVersion]
  private[this] val debugRequestReads = Json.reads[DebugRequest]

  private[this] val setPreferenceReads = Json.reads[SetPreference]

  private[this] val startBrawlReads = Json.reads[StartBrawl]
  private[this] val joinBrawlReads = Json.reads[JoinBrawl]
  private[this] val observeBrawlReads = Json.reads[ObserveBrawl]

  private[this] val selectTargetReads = Json.reads[SelectTarget]

  implicit val requestMessageReads = new Reads[RequestMessage] {
    override def reads(json: JsValue) = {
      val c = (json \ "c").as[String]
      val v = (json \ "v").as[JsValue]

      val jsResult = c match {
        case "MalformedRequest" => malformedRequestReads.reads(v)
        case "Ping" => pingReads.reads(v)
        case "GetVersion" => JsSuccess(GetVersion)
        case "DebugRequest" => debugRequestReads.reads(v)

        case "SetPreference" => setPreferenceReads.reads(v)

        case "StartBrawl" => startBrawlReads.reads(v)
        case "JoinBrawl" => joinBrawlReads.reads(v)
        case "ObserveBrawl" => observeBrawlReads.reads(v)

        case "SelectTarget" => selectTargetReads.reads(v)
        case "ResignBrawl" => JsSuccess(ResignBrawl)

        case "ActiveGemsLeft" => JsSuccess(ActiveGemsLeft)
        case "ActiveGemsRight" => JsSuccess(ActiveGemsRight)
        case "ActiveGemsClockwise" => JsSuccess(ActiveGemsClockwise)
        case "ActiveGemsCounterClockwise" => JsSuccess(ActiveGemsCounterClockwise)
        case "ActiveGemsStep" => JsSuccess(ActiveGemsStep)
        case "ActiveGemsDrop" => JsSuccess(ActiveGemsDrop)

        case _ => JsSuccess(MalformedRequest("UnknownType", s"c: $c, v: ${Json.stringify(v)}"))
      }
      jsResult match {
        case rm: JsSuccess[RequestMessage @unchecked] => rm
        case e: JsError =>
          val errors = e.errors.map(err => "[" + err._1.toString + ": " + err._2.map(x => x.message).mkString(", ") + "]").mkString(", ")
          val msg = s"Error parsing json for [$c]: $errors - ${Json.prettyPrint(v)}"
          throw new IllegalArgumentException(msg)
      }
    }
  }
}
