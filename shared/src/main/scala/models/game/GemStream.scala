package models.game

import scala.util.Random

case class GemStream(seed: Int) {
  private[this] val r = new Random(seed)
  private[this] var nextId = 0

  def next = {
    Gem(nextId, randomColor, crash = r.nextInt(5) == 0)
  }

  private[this] def randomColor = r.nextInt(41) match {
    case i if i == 40 => Color.Wild
    case i if i % 4 == 0 => Color.Red
    case i if i % 4 == 1 => Color.Green
    case i if i % 4 == 2 => Color.Blue
    case i if i % 4 == 3 => Color.Yellow
  }
}
