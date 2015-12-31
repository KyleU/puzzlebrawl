package models.brawl

import java.util.UUID

case class PlayerResult(
  id: UUID,
  name: String,
  script: Option[String],
  team: Int,
  score: Int,
  normalGemCount: Int,
  timerGemCount: Int,
  moveCount: Int)
