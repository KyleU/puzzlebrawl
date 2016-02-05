package models.brawl

import java.util.UUID

final case class PlayerResult(
  id: UUID,
  name: String,
  script: Option[String],
  team: Int,
  score: Int,
  normalGemCount: Int,
  timerGemCount: Int,
  moveCount: Int,
  status: String,
  completed: Option[Long])
