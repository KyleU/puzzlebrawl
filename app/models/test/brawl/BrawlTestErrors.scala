package models.test.brawl

trait BrawlTestErrors { this: BrawlTest =>
  def getErrors = {
    val spaceErrors = (0 until goal.board.height).flatMap { y =>
      (0 until goal.board.width).flatMap { x =>
        val src = test.board.at(x, y)
        val tgt = goal.board.at(x, y)
        if (src == tgt) {
          None
        } else {
          Some(BrawlTest.TestError(src, tgt, x, y))
        }
      }
    }
    val activeGemErrors = test.activeGems.indices.flatMap { i =>
      val src = test.activeGems.lift(i)
      val tgt = goal.activeGems.lift(i)
      if (src == tgt) {
        None
      } else {
        Some(BrawlTest.TestError(src.map(_.gem), tgt.map(_.gem), src.map(_.x).getOrElse(0), src.map(_.y).getOrElse(0)))
      }
    }
    val scoreErrors = if (test.score == goal.score) {
      Seq.empty
    } else {
      Seq(BrawlTest.TestError(None, None, 0, 0, Some(s"Observed score [${test.score}] did not match expected score [${goal.score}].")))
    }
    spaceErrors ++ activeGemErrors ++ scoreErrors
  }
}
