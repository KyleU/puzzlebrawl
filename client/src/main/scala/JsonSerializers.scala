import models._
import models.board.Board
import models.board.mutation.Mutation
import models.board.mutation.Mutation._
import models.brawl.Brawl
import models.gem.{ Color, Gem, GemLocation, GemStream }
import models.player.Player
import upickle._
import upickle.legacy._

object JsonSerializers {
  private implicit val stringOptionWriter = Writer[Option[String]] {
    case Some(s) => Js.Str(s)
    case None => Js.Null
  }
  private implicit val intOptionWriter = Writer[Option[Int]] {
    case Some(i) => Js.Num(i)
    case None => Js.Null
  }
  private implicit val boolOptionWriter = Writer[Option[Boolean]] {
    case Some(b) => if (b) { Js.True } else { Js.False }
    case None => Js.Null
  }

  private implicit val colorWrites = Writer[Color] { case c => Js.Str(c.charVal.toString) }

  private implicit val gemWriter = Writer[Gem] { case g => writeJs(g) }
  private implicit val gemLocationWriter = Writer[GemLocation] { case gl => writeJs(gl) }

  private implicit val spacesWrites = Writer[Array[Array[Option[Gem]]]] { case spaces =>
    val arr = Js.Arr(spaces.map { col =>
      Js.Arr(col.map {
        case Some(gem) => writeJs(gem)
        case None => Js.Null
      }: _*)
    }: _*)
    arr
  }

  private implicit val boardWriter = Writer[Board] { case b =>
    val json = writeJs(b)
    val spaces = writeJs(b.getSpacesCopy)
    val seq = json match {
      case o: Js.Obj =>
        val combined = o.value ++ Seq("spaces" -> spaces)
        combined
      case _ => throw new IllegalStateException()
    }
    Js.Obj(seq: _*)
  }
  private implicit val gemStreamWriter = Writer[GemStream] { case gs => writeJs(gs) }

  private implicit val playerWriter = Writer[Player] { case p => writeJs(p) }

  private implicit val brawlWriter = Writer[Brawl] { case b => writeJs(b) }
  private implicit val brawlJoinedWriter = Writer[BrawlJoined] { case bj => writeJs(bj) }

  implicit val mutationWrites = Writer[Mutation] { case m =>
    val v = m match {
      case ag: AddGem => "a" -> writeJs(ag)
      case mg: MoveGem => "m" -> writeJs(mg)
      case mgs: MoveGems => "x" -> Js.Arr(Js.Str("MoveGems"), Js.Obj("moves" -> Js.Arr(mgs.moves.map(move => writeJs(move).asInstanceOf[Js.Arr].value(1)): _*)))
      case cg: ChangeGem => "c" -> writeJs(cg)
      case rg: RemoveGem => "r" -> writeJs(rg)
    }
    Js.Obj("t" -> Js.Str(v._1), "v" -> v._2.asInstanceOf[Js.Arr].value(1))
  }

  private implicit val responseMessageWriter: Writer[ResponseMessage] = Writer[ResponseMessage] {
    case rm =>
      val jsVal = rm match {
        case vr: VersionResponse => writeJs(vr)
        case p: Pong => writeJs(p)
        case ms: MessageSet => writeJs(ms)

        case bj: BrawlJoined => writeJs(bj)
        case pu: PlayerUpdate => writeJs(pu)

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

  def write(rm: ResponseMessage) = responseMessageWriter.write(rm)
  def write(j: Js.Value) = json.write(j)
}
