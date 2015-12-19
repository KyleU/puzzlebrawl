package json

import models._
import upickle._
import upickle.legacy._

import BrawlSerializers._
import MutationSerializers._

object ResponseMessageSerializers {
  private implicit val responseMessageReader: Reader[ResponseMessage] = Reader[ResponseMessage] {
    case json: Js.Obj =>
      val c = json.value.find(_._1 == "c").getOrElse(throw new IllegalStateException())._2 match {
        case Js.Str(s) => s
        case _ => throw new IllegalStateException()
      }
      val v = json.value.find(_._1 == "v").getOrElse(throw new IllegalStateException())._2 match {
        case o: Js.Obj => o
        case _ => throw new IllegalStateException()
      }
      val ret: ResponseMessage = json.value.find(_._1 == "v").getOrElse(throw new IllegalStateException())._2 match {
        case o: Js.Obj => c match {
          case "VersionResponse" => readJs[VersionResponse](o)
          case "Pong" => readJs[Pong](o)
          case "MessageSet" => readJs[MessageSet](o)
          case "BrawlJoined" => readJs[BrawlJoined](o)
          case "PlayerUpdate" => readJs[PlayerUpdate](o)
          case "ServerError" => readJs[ServerError](o)
          case _ => throw new IllegalStateException()
        }
        case _ => throw new IllegalStateException()
      }
      ret
  }

  private implicit val responseMessageWriter: Writer[ResponseMessage] = Writer[ResponseMessage] {
    case rm =>
      val jsVal = rm match {
        case vr: VersionResponse => writeJs(vr)
        case p: Pong => writeJs(p)
        case ms: MessageSet => writeJs(ms)
        case bj: BrawlJoined => writeJs(bj)
        case pu: PlayerUpdate => writeJs(pu)
        case se: ServerError => writeJs(se)

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

  def read(json: Js.Value) = readJs[ResponseMessage](json)
  def write(rm: ResponseMessage) = writeJs(rm)
}
