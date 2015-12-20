package json

import models.board.mutation.Mutation
import models.board.mutation.Mutation._
import upickle._
import upickle.legacy._

import BaseSerializers.{ intOptionWriter, intOptionReader }
import BrawlSerializers.{ gemWriter, gemReader }

object MutationSerializers {
  implicit val mutationWriter = Writer[Mutation] {
    case m =>
      val v = m match {
        case ag: AddGem => "a" -> writeJs(ag)
        case mg: MoveGem => "m" -> writeJs(mg)
        case mgs: MoveGems => "x" -> Js.Arr(Js.Str("MoveGems"), Js.Obj(
          "moves" -> Js.Arr(mgs.moves.map(move => writeJs(move).asInstanceOf[Js.Arr].value(1)): _*)
        ))
        case cg: ChangeGem => "c" -> writeJs(cg)
        case rg: RemoveGem => "r" -> writeJs(rg)
        case tc: TargetChanged=> "t" -> writeJs(tc)
      }
      Js.Obj("t" -> Js.Str(v._1), "v" -> v._2.asInstanceOf[Js.Arr].value(1))
  }

  private implicit val mutationReader = Reader[Mutation] {
    case json: Js.Obj =>
      val t = json.value.find(_._1 == "t").getOrElse(throw new IllegalStateException())._2 match {
        case Js.Str(s) => s
        case _ => throw new IllegalStateException()
      }
      val v: Mutation = json.value.find(_._1 == "v").getOrElse(throw new IllegalStateException())._2 match {
        case o: Js.Obj => t match {
          case "a" => readJs[AddGem](o)
          case "m" => readJs[MoveGem](o)
          case "x" => readJs[MoveGems](o)
          case "c" => readJs[ChangeGem](o)
          case "r" => readJs[RemoveGem](o)
          case "t" => readJs[TargetChanged](o)
          case _ => throw new IllegalStateException()
        }
        case _ => throw new IllegalStateException()
      }
      v
  }

}
