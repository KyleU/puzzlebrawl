import models.board.Board
import models.brawl.Brawl
import models.gem.{ Color, Gem, GemLocation, GemStream }
import models.player.Player
import upickle._
import upickle.legacy._

object BrawlSerializers {
  import BaseSerializers._

  private implicit val colorWrites = Writer[Color] { case c => Js.Str(c.charVal.toString) }

  implicit val gemWriter = Writer[Gem] { case g => writeJs(g) }
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

  implicit val brawlWriter = Writer[Brawl] { case b => writeJs(b) }
}
