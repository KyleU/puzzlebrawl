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

  object Charging {
    val normalGemCharge = 1.0
    val bonusGemCharge = 1.2
    val wildPerGemCharge = 0.8
    val wildSoloDropCharge = 10.0
  }

  object Scoring {
    val normalGemScore = 100
    val bonusGemScore = 120
    val wildPerGemScore = 80
    val wildSoloDropScore = 1000
  }
}
