package models.game.test

import models.game.gem.{ Color, Gem }

object GameTestCollapse extends GameTest.Provider {
  override def newInstance() = GameTestCollapse()
}

case class GameTestCollapse() extends GameTest(seed = 0) {
  override def init() = {
    board.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 1)
    board.add(Gem(1, Color.Green), 1, 4)
    board.add(Gem(2, Color.Blue), 2, 5)
    board.add(Gem(3, Color.Blue), 3, 7)
    board.add(Gem(4, Color.Green), 4, 9)
    board.add(Gem(5, Color.Red), 5, 11)
    board.add(Gem(6, Color.Yellow), 2, 6)
    board.add(Gem(7, Color.Yellow), 3, 9)

    goal.add(Gem(0, Color.Red, width = Some(2), height = Some(2)), 0, 0)
    goal.add(Gem(1, Color.Green), 1, 2)
    goal.add(Gem(2, Color.Blue), 2, 0)
    goal.add(Gem(3, Color.Blue), 3, 0)
    goal.add(Gem(4, Color.Green), 4, 0)
    goal.add(Gem(5, Color.Red), 5, 0)
    goal.add(Gem(6, Color.Yellow), 2, 1)
    goal.add(Gem(7, Color.Yellow), 3, 1)
  }

  override def run() = board.collapse()
}
