package models

object Constants {
  object Board {
    val defaultWidth = 6
    val defaultHeight = 12
  }

  object GemStream {
    val baseWildGemChance = 0.01
    val baseCrashGemChance = 0.15
  }

  object Scoring {
    val normalGemScore = 100
    val largeGemScore = 150
    val wildPerGemScore = 50
    val wildSoloDropScore = 1000
  }
}
