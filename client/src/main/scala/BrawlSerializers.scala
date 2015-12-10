import models.board.Board
import models.brawl.Brawl
import models.gem.{ Color, Gem, GemLocation, GemStream }
import models.player.Player
import upickle._
import upickle.legacy._

object BrawlSerializers {
  import BaseSerializers._

  private implicit val colorWriter = Writer[Color] { case c => Js.Str(c.charVal.toString) }
  private implicit val colorReader = Reader[Color] { case Js.Str(s) => Color.fromChar(s.head) }

  implicit val gemWriter = Writer[Gem] { case g => writeJs(g) }
  implicit val gemReader = Reader[Gem] { case json => readJs[Gem](json) }

  private implicit val gemLocationWriter = Writer[GemLocation] { case gl => writeJs(gl) }
  private implicit val gemLocationReader = Reader[GemLocation] { case json => readJs[GemLocation](json) }

  private implicit val spacesWriter = Writer[Array[Array[Option[Gem]]]] { case spaces =>
    val arr = Js.Arr(spaces.map { col =>
      Js.Arr(col.map {
        case Some(gem) => writeJs(gem)
        case None => Js.Null
      }: _*)
    }: _*)
    arr
  }
  private implicit val spacesReader = Reader[Array[Array[Option[Gem]]]] {
    case js: Js.Arr => Array.empty
  }

  private implicit val boardWriter = Writer[Board] { case b =>
    val json = writeJs(b)
    val spaces = writeJs(b.getSpacesCopy)
    val seq = json match {
      case o: Js.Obj => o.value ++ Seq("spaces" -> spaces)
      case _ => throw new IllegalStateException()
    }
    Js.Obj(seq: _*)
  }
  private implicit val boardReader = Reader[Board] { case json =>
    val ret = readJs[Board](json)
    ret.
    ret
  }

  private implicit val gemStreamWriter = Writer[GemStream] { case gs => writeJs(gs) }
  private implicit val gemStreamReader = Reader[GemStream] { case json => readJs[GemStream](json) }

  private implicit val playerWriter = Writer[Player] { case p => writeJs(p) }
  private implicit val playerReader = Reader[Player] { case json => readJs[Player](json) }

  implicit val brawlWriter = Writer[Brawl] { case b => writeJs(b) }
  implicit val brawlReader = Reader[Brawl] { case json => readJs[Brawl](json) }
}
