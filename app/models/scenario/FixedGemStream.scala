package models.scenario

import models.gem.{ Gem, GemStream }

class FixedGemStream(val gems: Seq[Gem]) extends GemStream {
  private[this] var remaining = gems

  override def next = remaining.headOption match {
    case Some(n) =>
      remaining = remaining.tail
      n
    case None => throw new IllegalStateException("No more gems for fixed stream.")
  }
}
