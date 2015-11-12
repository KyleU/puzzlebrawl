package services.console.test

import com.googlecode.lanterna.input.{ KeyStroke, KeyType }
import models.game.gem.{ FuseRole, Gem }

object ConsoleTestFuse extends ConsoleBinaryTest(6, 12) {
  override def init() = {
    board.add(Gem(0, Gem.Red), 0, 0)
    board.add(Gem(1, Gem.Red), 1, 0)
    board.add(Gem(2, Gem.Red), 2, 0)
    board.add(Gem(3, Gem.Red), 0, 1)
    board.add(Gem(4, Gem.Red), 1, 1)
    board.add(Gem(5, Gem.Red), 2, 1)
    board.add(Gem(6, Gem.Red), 0, 2)
    board.add(Gem(7, Gem.Red), 1, 2)
    board.add(Gem(8, Gem.Red), 2, 2)
    board.add(Gem(9, Gem.Green), 0, 3)
    board.add(Gem(10, Gem.Blue, timer = Some(5)), 3, 0)

    test.add(Gem(0, Gem.Red, group = Some((1, FuseRole.BottomLeft))), 0, 0)
    test.add(Gem(1, Gem.Red, group = Some((1, FuseRole.Bottom))), 1, 0)
    test.add(Gem(2, Gem.Red, group = Some((1, FuseRole.BottomRight))), 2, 0)
    test.add(Gem(3, Gem.Red, group = Some((1, FuseRole.Left))), 0, 1)
    test.add(Gem(4, Gem.Red, group = Some((1, FuseRole.Center))), 1, 1)
    test.add(Gem(5, Gem.Red, group = Some((1, FuseRole.Right))), 2, 1)
    test.add(Gem(6, Gem.Red, group = Some((1, FuseRole.TopLeft))), 0, 2)
    test.add(Gem(7, Gem.Red, group = Some((1, FuseRole.Top))), 1, 2)
    test.add(Gem(8, Gem.Red, group = Some((1, FuseRole.TopRight))), 2, 2)
    test.add(Gem(9, Gem.Green), 0, 3)
    test.add(Gem(10, Gem.Blue, timer = Some(5)), 3, 0)
  }

  override def run() = board.fuse()
}
