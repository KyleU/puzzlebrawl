package models.test.gem

import models.gem.GemStream

object MockGemStreams {
  val allRed = GemStream(
    seed = 0,
    gemAdjustWild =  Some(0.0),
    gemAdjustCrash =  Some(0.0),
    gemAdjustGreen =  Some(0.0),
    gemAdjustBlue =  Some(0.0),
    gemAdjustYellow =  Some(0.0),
    crashAdjustGreen =  Some(0.0),
    crashAdjustBlue =  Some(0.0),
    crashAdjustYellow =  Some(0.0)
  )

  val allGreen = GemStream(
    seed = 0,
    gemAdjustWild =  Some(0.0),
    gemAdjustCrash =  Some(0.0),
    gemAdjustRed =  Some(0.0),
    gemAdjustBlue =  Some(0.0),
    gemAdjustYellow =  Some(0.0),
    crashAdjustRed =  Some(0.0),
    crashAdjustBlue =  Some(0.0),
    crashAdjustYellow =  Some(0.0)
  )

  val allBlue = GemStream(
    seed = 0,
    gemAdjustWild =  Some(0.0),
    gemAdjustCrash =  Some(0.0),
    gemAdjustRed =  Some(0.0),
    gemAdjustGreen =  Some(0.0),
    gemAdjustYellow =  Some(0.0),
    crashAdjustRed =  Some(0.0),
    crashAdjustGreen =  Some(0.0),
    crashAdjustYellow =  Some(0.0)
  )

  val allYellow = GemStream(
    seed = 0,
    gemAdjustWild =  Some(0.0),
    gemAdjustCrash =  Some(0.0),
    gemAdjustRed =  Some(0.0),
    gemAdjustGreen =  Some(0.0),
    gemAdjustBlue =  Some(0.0),
    crashAdjustRed =  Some(0.0),
    crashAdjustGreen =  Some(0.0),
    crashAdjustBlue =  Some(0.0)
  )

  val allRedBlue = GemStream(
    seed = 0,
    gemAdjustWild =  Some(0.0),
    gemAdjustGreen =  Some(0.0),
    gemAdjustYellow =  Some(0.0),
    crashAdjustGreen =  Some(0.0),
    crashAdjustYellow =  Some(0.0)
  )

  val allCrash = GemStream(
    seed = 0,
    gemAdjustWild =  Some(0.0),
    gemAdjustCrash = Some(10.0)
  )

  val allWild = GemStream(
    seed = 0,
    gemAdjustWild =  Some(100.0)
  )
}
