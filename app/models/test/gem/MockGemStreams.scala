package models.test.gem

import models.gem.GemStream

object MockGemStreams {
  def forString(s: String) = s match {
    case "Red" => allRed()
    case "Green" => allGreen()
    case "Blue" => allBlue()
    case "Yellow" => allYellow()
    case "Red/Blue" => allRedBlue()
    case "Crash" => allCrash()
    case "Wild" => allWild()
    case _ => throw new IllegalArgumentException(s"Invalid mock gem stream [$s].")
  }

  private[this] def allRed() = GemStream(
    seed = 0,
    gemAdjustWild = Some(0.0),
    gemAdjustCrash = Some(0.0),
    gemAdjustGreen = Some(0.0),
    gemAdjustBlue = Some(0.0),
    gemAdjustYellow = Some(0.0),
    crashAdjustGreen = Some(0.0),
    crashAdjustBlue = Some(0.0),
    crashAdjustYellow = Some(0.0)
  )

  private[this] def allGreen() = GemStream(
    seed = 0,
    gemAdjustWild = Some(0.0),
    gemAdjustCrash = Some(0.0),
    gemAdjustRed = Some(0.0),
    gemAdjustBlue = Some(0.0),
    gemAdjustYellow = Some(0.0),
    crashAdjustRed = Some(0.0),
    crashAdjustBlue = Some(0.0),
    crashAdjustYellow = Some(0.0)
  )

  private[this] def allBlue() = GemStream(
    seed = 0,
    gemAdjustWild = Some(0.0),
    gemAdjustCrash = Some(0.0),
    gemAdjustRed = Some(0.0),
    gemAdjustGreen = Some(0.0),
    gemAdjustYellow = Some(0.0),
    crashAdjustRed = Some(0.0),
    crashAdjustGreen = Some(0.0),
    crashAdjustYellow = Some(0.0)
  )

  private[this] def allYellow() = GemStream(
    seed = 0,
    gemAdjustWild = Some(0.0),
    gemAdjustCrash = Some(0.0),
    gemAdjustRed = Some(0.0),
    gemAdjustGreen = Some(0.0),
    gemAdjustBlue = Some(0.0),
    crashAdjustRed = Some(0.0),
    crashAdjustGreen = Some(0.0),
    crashAdjustBlue = Some(0.0)
  )

  private[this] def allRedBlue() = GemStream(
    seed = 0,
    gemAdjustWild = Some(0.0),
    gemAdjustGreen = Some(0.0),
    gemAdjustYellow = Some(0.0),
    crashAdjustGreen = Some(0.0),
    crashAdjustYellow = Some(0.0)
  )

  private[this] def allCrash() = GemStream(
    seed = 0,
    gemAdjustWild = Some(0.0),
    gemAdjustCrash = Some(10.0)
  )

  private[this] def allWild() = GemStream(
    seed = 0,
    gemAdjustWild = Some(100.0)
  )
}
