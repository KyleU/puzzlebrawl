package models.game

case class Board(width: Int, height: Int) {
  private val spaces = Array.ofDim[Option[Gem]](height, width)
  for(x <- spaces) {
    for(y <- x.indices) {
      x(y) = None
    }
  }

  def add(gem: Gem, col: Int) = emptyRowSpace(col).foreach( x => spaces(x)(col) = Some(gem))

  def emptyRowSpace(col: Int) = spaces.lastIndexWhere(_(col).isDefined) match {
    case -1 => Some(0)
    case x if x == height => None
    case x => Some(x + 1)
  }

  override def toString = spaces.reverse.map { row =>
    row.map {
      case Some(gem) => gem.toChar
      case None => "."
    }.mkString
  }.mkString("\n")
}
