
trait CheatHelper { this: PuzzleBrawl =>
  protected[this] def handleCheat(key: String) = {
    println(s"Cheat [$key] ignored.")
  }
}
