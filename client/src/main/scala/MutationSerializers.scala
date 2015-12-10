import models.board.mutation.Mutation
import models.board.mutation.Mutation._
import upickle._
import upickle.legacy._

import BrawlSerializers.gemWriter

object MutationSerializers {
  implicit val mutationWriter = Writer[Mutation] { case m =>
    val v = m match {
      case ag: AddGem => "a" -> writeJs(ag)
      case mg: MoveGem => "m" -> writeJs(mg)
      case mgs: MoveGems => "x" -> Js.Arr(Js.Str("MoveGems"), Js.Obj("moves" -> Js.Arr(mgs.moves.map(move => writeJs(move).asInstanceOf[Js.Arr].value(1)): _*)))
      case cg: ChangeGem => "c" -> writeJs(cg)
      case rg: RemoveGem => "r" -> writeJs(rg)
    }
    Js.Obj("t" -> Js.Str(v._1), "v" -> v._2.asInstanceOf[Js.Arr].value(1))
  }
}
