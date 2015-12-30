package services.brawl

trait CheatHelper { this: BrawlService =>
  protected[this] def handleCheat(key: String) = {
    println(s"Cheat [$key] ignored.")
  }
}
