package models.game.gem

import scala.util.Random

object GemStream {
  val baseWildGemChance = 0.01
  val baseCrashGemChance = 0.1
}

case class GemStream(
  seed: Int,

  gemAdjustWild: Double = 1.0,
  gemAdjustCrash: Double = 1.0,

  gemAdjustRed: Double = 1.0,
  gemAdjustGreen: Double = 1.0,
  gemAdjustBlue: Double = 1.0,
  gemAdjustYellow: Double = 1.0,

  crashAdjustRed: Double = 1.0,
  crashAdjustGreen: Double = 1.0,
  crashAdjustBlue: Double = 1.0,
  crashAdjustYellow: Double = 1.0
) {
  private[this] val r = new Random(seed)
  private[this] var nextId = 0

  private[this] val wildChance = GemStream.baseWildGemChance * gemAdjustWild
  private[this] val crashChance = GemStream.baseWildGemChance * gemAdjustCrash

  val baseChanceValue = 100.0

  private[this] val gemChances = Seq(
    Gem.Red -> baseChanceValue * gemAdjustRed,
    Gem.Green -> baseChanceValue * gemAdjustGreen,
    Gem.Blue -> baseChanceValue * gemAdjustBlue,
    Gem.Yellow -> baseChanceValue * gemAdjustYellow
  )
  private[this] val gemChanceTotal = gemChances.map(_._2).sum

  private[this] val crashChances = Seq(
    Gem.Red -> baseChanceValue * crashAdjustRed,
    Gem.Green -> baseChanceValue * crashAdjustGreen,
    Gem.Blue -> baseChanceValue * crashAdjustBlue,
    Gem.Yellow -> baseChanceValue * crashAdjustYellow
  )
  private[this] val crashChanceTotal = crashChances.map(_._2).sum

  def next = {
    val diceRoll = r.nextDouble
    val ret = if(diceRoll < wildChance) {
      Gem(nextId, color = Gem.Wild)
    } else {
      val crash = diceRoll < crashChance
      val color = randomColor(crash)
      Gem(nextId, color = color, crash = crash)
    }
    nextId += 1
    ret
  }

  private[this] def randomColor(crash: Boolean): Gem.Color = {
    val dist = if(crash) { crashChances } else { gemChances }
    val p = r.nextDouble * (if(crash) { crashChanceTotal } else { gemChanceTotal })
    var accum = 0.0
    val it = dist.iterator
    while (it.hasNext) {
      val (item, itemProb) = it.next
      accum += itemProb
      if (accum >= p) {
        return item
      }
    }
    throw new IllegalStateException()
  }
}
