package utils.json

import models._
import play.api.libs.json._

object RequestMessageSerializers {
  private[this] val malformedRequestReads = Json.reads[MalformedRequest]
  private[this] val pingReads = Json.reads[Ping]
  // case object [GetVersion]
  private[this] val debugInfoReads = Json.reads[DebugInfo]

  private[this] val setPreferenceReads = Json.reads[SetPreference]

  private[this] val startBrawlReads = Json.reads[StartBrawl]

  implicit val requestMessageReads = new Reads[RequestMessage] {
    override def reads(json: JsValue) = {
      val c = (json \ "c").as[String]
      val v = (json \ "v").as[JsValue]

      val jsResult = c match {
        case "MalformedRequest" => malformedRequestReads.reads(v)
        case "Ping" => pingReads.reads(v)
        case "GetVersion" => JsSuccess(GetVersion)
        case "DebugInfo" => debugInfoReads.reads(v)

        case "SetPreference" => setPreferenceReads.reads(v)

        case "StartBrawl" => startBrawlReads.reads(v)

        case "ActiveGemsLeft" => JsSuccess(ActiveGemsLeft)
        case "ActiveGemsRight" => JsSuccess(ActiveGemsRight)
        case "ActiveGemsClockwise" => JsSuccess(ActiveGemsClockwise)
        case "ActiveGemsCounterClockwise" => JsSuccess(ActiveGemsCounterClockwise)
        case "ActiveGemsStep" => JsSuccess(ActiveGemsStep)

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
