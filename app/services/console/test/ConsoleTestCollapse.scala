package services.console.test

import models.game.gem.Gem

object ConsoleTestCollapse extends ConsoleBinaryTest(6, 12) {
  override def init() = {
    board.add(Gem(0, Gem.Red), 0, 0)
    board.add(Gem(1, Gem.Green), 1, 2)
    board.add(Gem(2, Gem.Blue), 2, 4)
    board.add(Gem(3, Gem.Blue), 3, 7)
    board.add(Gem(4, Gem.Green), 4, 9)
    board.add(Gem(5, Gem.Red), 5, 11)
    board.add(Gem(6, Gem.Yellow), 2, 6)
    board.add(Gem(7, Gem.Yellow), 3, 9)

    test.add(Gem(0, Gem.Red), 0, 0)
    test.add(Gem(1, Gem.Green), 1, 0)
    test.add(Gem(2, Gem.Blue), 2, 0)
    test.add(Gem(3, Gem.Blue), 3, 0)
    test.add(Gem(4, Gem.Green), 4, 0)
    test.add(Gem(5, Gem.Red), 5, 0)
    test.add(Gem(6, Gem.Yellow), 2, 1)
    test.add(Gem(7, Gem.Yellow), 3, 1)
  }

  override def run() = board.collapse()
}
