package services.console.test

import models.game.gem.Gem

object ConsoleTestCrash extends ConsoleBinaryTest(6, 12) {
  override def init() = {
    board.add(Gem(0, Gem.Red), 0, 0)
    board.add(Gem(1, Gem.Red, crash = true), 1, 0)
    board.add(Gem(2, Gem.Red), 2, 0)
    board.add(Gem(3, Gem.Red), 3, 0)
    board.add(Gem(4, Gem.Red), 3, 1)
    board.add(Gem(5, Gem.Blue), 0, 1)
    board.add(Gem(6, Gem.Green), 3, 2)

    test.add(Gem(5, Gem.Blue), 0, 0)
    test.add(Gem(6, Gem.Green), 3, 0)
  }

  override def run() = {
    board.crash()
    board.collapse()
  }
}
