package services.console

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyStroke
import models.game.{ FuseRole, Gem, Board }

object ConsoleTestB extends ConsoleTest {
  lazy val board = {
    val b = Board(6, 12)
    ConsoleBorders.render(client, 0, 0, b.width, b.height, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK)

    b.add(Gem(0, Gem.Red), 0, 0)
    b.add(Gem(1, Gem.Red), 1, 0)
    b.add(Gem(2, Gem.Red), 2, 0)
    b.add(Gem(3, Gem.Red), 0, 1)
    b.add(Gem(4, Gem.Red), 1, 1)
    b.add(Gem(5, Gem.Red), 2, 1)
    b.add(Gem(6, Gem.Red), 0, 2)
    b.add(Gem(7, Gem.Red), 1, 2)
    b.add(Gem(8, Gem.Red), 2, 2)
    b.add(Gem(9, Gem.Green), 0, 3)
    b.add(Gem(10, Gem.Blue, timer = Some(5)), 3, 0)
    b
  }

  lazy val test = {
    val b = Board(6, 12)
    ConsoleBorders.render(client, 15, 0, b.width, b.height, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK)

    b.add(Gem(0, Gem.Red, fuseRole = Some(FuseRole.BottomLeft)), 0, 0)
    b.add(Gem(1, Gem.Red, fuseRole = Some(FuseRole.Bottom)), 1, 0)
    b.add(Gem(2, Gem.Red, fuseRole = Some(FuseRole.BottomRight)), 2, 0)
    b.add(Gem(3, Gem.Red, fuseRole = Some(FuseRole.Left)), 0, 1)
    b.add(Gem(4, Gem.Red, fuseRole = Some(FuseRole.Center)), 1, 1)
    b.add(Gem(5, Gem.Red, fuseRole = Some(FuseRole.Right)), 2, 1)
    b.add(Gem(6, Gem.Red, fuseRole = Some(FuseRole.TopLeft)), 0, 2)
    b.add(Gem(7, Gem.Red, fuseRole = Some(FuseRole.Top)), 1, 2)
    b.add(Gem(8, Gem.Red, fuseRole = Some(FuseRole.TopRight)), 2, 2)
    b.add(Gem(9, Gem.Green), 0, 3)
    b.add(Gem(10, Gem.Blue, timer = Some(5)), 3, 0)
    b
  }


  override def init() = {
    client.add(board, 1, 1)
    client.add(test, 16, 1)
  }

  def fuse(b: Board, g: Gem, x: Int, y: Int): Option[Seq[Int]] = {
    val u = b.spaces(x)(y + 1)
    val r = b.spaces(x + 1)(y)
    None
  }

  override def inputCharacter(input: KeyStroke) = input.getCharacter match {
    case x if x == ' ' =>
      board.drop(client.gemStream.next, r.nextInt(board.width))
      client.render()
      true
    case x if x == 'f' =>
      true
    case _ => false
  }
}
