package models

object Constants {
  object Board {
    val defaultWidth = 6
    val defaultHeight = 12
  }

  object GemStream {
    val baseWildGemInterval = 50
    val baseCrashGemChance = 0.2
  }

  object Scoring {
    val normalGemScore = 100
    val largeGemScore = 200
    val wildPerGemScore = 80
    val wildSoloDropScore = 1000
  }
}
