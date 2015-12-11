import models._
import upickle._
import upickle.legacy._

import BrawlSerializers._

object RequestMessageSerializers {
  private implicit val requestMessageReader: Reader[RequestMessage] = Reader[RequestMessage] { case json: Js.Obj =>
    val c = json.value.find(_._1 == "c").getOrElse(throw new IllegalStateException())._2 match {
      case Js.Str(s) => s
      case _ => throw new IllegalStateException()
    }
    val v = json.value.find(_._1 == "v").getOrElse(throw new IllegalStateException())._2 match {
      case o: Js.Obj => o
      case _ => throw new IllegalStateException()
    }
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

  private implicit val requestMessageWriter: Writer[RequestMessage] = Writer[RequestMessage] { case rm =>
    val jsVal = rm match {
      case mf: MalformedRequest => writeJs(mf)
      case p: Ping => writeJs(p)
      case GetVersion => Js.Obj
      case dr: DebugRequest => writeJs(dr)

      case sp: SetPreference => writeJs(sp)

      case sb: StartBrawl => writeJs(sb)

      case _: ActiveGemsMessage => Js.Obj

      case _ => throw new IllegalStateException(s"Invalid Message [${rm.getClass.getName}].")
    }
    val jsArray = jsVal match { case arr: Js.Arr => arr; case _ => throw new IllegalArgumentException(jsVal.toString) }
    jsArray.value.toList match {
      case one :: two :: Nil =>
        val oneStr = Js.Str(one match {
          case s: Js.Str => s.value.replace("models.", "")
          case _ => throw new IllegalStateException()
        })
        Js.Obj("c" -> oneStr, "v" -> two)
      case _ => throw new IllegalStateException()
    }
  }
}
