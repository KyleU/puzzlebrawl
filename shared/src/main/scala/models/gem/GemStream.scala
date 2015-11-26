package models.gem

import scala.util.Random

object GemStream {
  val baseWildGemChance = 0.01
  val baseCrashGemChance = 0.1
}

case class GemStream(
    seed: Int,

    gemAdjustWild: Option[Double] = None,
    gemAdjustCrash: Option[Double] = None,

    gemAdjustRed: Option[Double] = None,
    gemAdjustGreen: Option[Double] = None,
    gemAdjustBlue: Option[Double] = None,
    gemAdjustYellow: Option[Double] = None,

    crashAdjustRed: Option[Double] = None,
    crashAdjustGreen: Option[Double] = None,
    crashAdjustBlue: Option[Double] = None,
    crashAdjustYellow: Option[Double] = None
) {
  private[this] val r = new Random(seed)
  private[this] var nextId = 0

  private[this] val wildChance = GemStream.baseWildGemChance * gemAdjustWild.getOrElse(1.0)
  private[this] val crashChance = GemStream.baseCrashGemChance * gemAdjustCrash.getOrElse(1.0)

  val baseChanceValue = 100.0

  private[this] val gemChances = Seq(
    Color.Red -> baseChanceValue * gemAdjustRed.getOrElse(1.0),
    Color.Green -> baseChanceValue * gemAdjustGreen.getOrElse(1.0),
    Color.Blue -> baseChanceValue * gemAdjustBlue.getOrElse(1.0),
    Color.Yellow -> baseChanceValue * gemAdjustYellow.getOrElse(1.0)
  )
  private[this] val gemChanceTotal = gemChances.map(_._2).sum

  private[this] val crashChances = Seq(
    Color.Red -> baseChanceValue * crashAdjustRed.getOrElse(1.0),
    Color.Green -> baseChanceValue * crashAdjustGreen.getOrElse(1.0),
    Color.Blue -> baseChanceValue * crashAdjustBlue.getOrElse(1.0),
    Color.Yellow -> baseChanceValue * crashAdjustYellow.getOrElse(1.0)
  )
  private[this] val crashChanceTotal = crashChances.map(_._2).sum

  def next = {
    val ret = if (r.nextDouble < wildChance) {
      Gem(nextId, color = Color.Wild)
    } else {
      val crash = if (r.nextDouble < crashChance) { Some(true) } else { None }
      val color = randomColor(crash.exists(x => x))
      Gem(nextId, color = color, crash = crash)
    }
    nextId += 1
    ret
  }

  private[this] def randomColor(crash: Boolean): Color = {
    val dist = if (crash) { crashChances } else { gemChances }
    val p = r.nextDouble * (if (crash) { crashChanceTotal } else { gemChanceTotal })
    var accum = 0.0
    dist.iterator.find { i =>
      val (item, itemProb) = i
      accum += itemProb
      accum >= p
    }.map(_._1).getOrElse(throw new IllegalStateException())
  }
}
