package models.gem

object GemPattern {
  val default = horizontal

  lazy val all = Seq(horizontal, vertical, boxes)

  lazy val horizontal = GemPattern("horizontal", Seq(
    Seq(Color.Red, Color.Red, Color.Red, Color.Red, Color.Red, Color.Red),
    Seq(Color.Green, Color.Green, Color.Green, Color.Green, Color.Green, Color.Green),
    Seq(Color.Blue, Color.Blue, Color.Blue, Color.Blue, Color.Blue, Color.Blue),
    Seq(Color.Yellow, Color.Yellow, Color.Yellow, Color.Yellow, Color.Yellow, Color.Yellow)
  ))

  lazy val vertical = GemPattern("vertical", Seq(
    Seq(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Red, Color.Green),
    Seq(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Red, Color.Green),
    Seq(Color.Blue, Color.Yellow, Color.Red, Color.Green, Color.Blue, Color.Yellow),
    Seq(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Red, Color.Green)
  ))

  lazy val boxes = GemPattern("boxes", Seq(
    Seq(Color.Red, Color.Red, Color.Green, Color.Green, Color.Blue, Color.Blue),
    Seq(Color.Red, Color.Red, Color.Green, Color.Green, Color.Blue, Color.Blue),
    Seq(Color.Yellow, Color.Yellow, Color.Red, Color.Red, Color.Green, Color.Green),
    Seq(Color.Yellow, Color.Yellow, Color.Red, Color.Red, Color.Green, Color.Green)
  ))
}

case class GemPattern(key: String, rows: Seq[Seq[Color]]) {
  val height = rows.length
  val width = rows.map(_.length).max

  if (height != 4 || width != 6) {
    throw new IllegalStateException("Invalid pattern size [, ].")
  }
}
