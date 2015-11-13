package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestCollapse extends GameTest(seed = 0, width = 6, height = 12) {
  override def init() = {
    board.add(Gem(0, Color.Red), 0, 0)
    board.add(Gem(1, Color.Green), 1, 2)
    board.add(Gem(2, Color.Blue), 2, 4)
    board.add(Gem(3, Color.Blue), 3, 7)
    board.add(Gem(4, Color.Green), 4, 9)
    board.add(Gem(5, Color.Red), 5, 11)
    board.add(Gem(6, Color.Yellow), 2, 6)
    board.add(Gem(7, Color.Yellow), 3, 9)

    testBoard.add(Gem(0, Color.Red), 0, 0)
    testBoard.add(Gem(1, Color.Green), 1, 0)
    testBoard.add(Gem(2, Color.Blue), 2, 0)
    testBoard.add(Gem(3, Color.Blue), 3, 0)
    testBoard.add(Gem(4, Color.Green), 4, 0)
    testBoard.add(Gem(5, Color.Red), 5, 0)
    testBoard.add(Gem(6, Color.Yellow), 2, 1)
    testBoard.add(Gem(7, Color.Yellow), 3, 1)
  }

  override def run() = board.collapse()
}
