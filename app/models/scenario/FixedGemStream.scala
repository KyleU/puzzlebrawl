package models.scenario

import models.gem.{ Gem, GemStream }

class FixedGemStream(val gems: Seq[Gem]) extends GemStream {
  private[this] var remaining = gems

  override def next = {
    val n = remaining.head
    remaining = remaining.tail
    n
  }
}
