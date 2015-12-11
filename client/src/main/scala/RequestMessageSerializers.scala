import models._
import upickle._
import upickle.legacy._

import BrawlSerializers._

object RequestMessageSerializers {
  private implicit val requestMessageReader: Reader[RequestMessage] = Reader[RequestMessage] { case json: Js.Obj =>
    val c = "TODO"
    val v = Js.Obj()
    val ret: RequestMessage = c match {
      case "MalformedRequest" => readJs[MalformedRequest](v)
      case "Ping" => readJs[Ping](v)
      case "GetVersion" => GetVersion
      case "DebugRequest" => readJs[DebugRequest](v)

      case "SetPreference" => readJs[SetPreference](v)

      case "StartBrawl" => readJs[StartBrawl](v)

      case "ActiveGemsLeft" => ActiveGemsLeft
      case "ActiveGemsRight" => ActiveGemsRight
      case "ActiveGemsClockwise" => ActiveGemsClockwise
      case "ActiveGemsCounterClockwise" => ActiveGemsCounterClockwise
      case "ActiveGemsStep" => ActiveGemsStep
      case "ActiveGemsDrop" => ActiveGemsDrop

      case _ => MalformedRequest("UnknownType", s"c: $c, v: ${v.toString}")
    }
    ret
  }
}
