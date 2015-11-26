package models.game.test

import com.googlecode.lanterna.TextColor
import models.game.board.Board
import models.game.gem.{ Color, Gem }
import services.console.ConsoleBorders._

object TextGemPattern {
  def single(board: Board, gemOpt: Option[Gem], x: Int, y: Int) = {
    board.at(x, y) match {
      case None => ' '
      case Some(gem) if gem.timer.isDefined => gem.timer.getOrElse(0).toString.head
      case Some(gem) if gem.width.isDefined || gem.height.isDefined => character(board, gem, x, y)
      case Some(gem) if gem.color == Color.Wild => 'W'
      case Some(gem) => ' '
    }
  }

  def pair(board: Board, gemOpt: Option[Gem], x: Int, y: Int) = {
    gemOpt match {
      case None => (' ', ' ', TextColor.ANSI.WHITE)
      case Some(gem) if gem.timer.isDefined => (gem.timer.getOrElse(0).toString.head, gem.timer.getOrElse(0).toString.head, getColor(gem.color))
      case Some(gem) if gem.width.isDefined || gem.height.isDefined =>
        val chars = characterPair(board, gem, x, y)
        (chars._1, chars._2, getColor(gem.color))
      case Some(gem) => gem.crash match {
        case Some(true) => ('(', ')', getColor(gem.color))
        case _ => ('[', ']', getColor(gem.color))
      }
    }
  }

  private[this] def character(b: Board, gem: Gem, x: Int, y: Int) = {
    def check(x2: Int, y2: Int) = b.at(x2, y2).exists(_.id == gem.id)
    val (u, r, d, l) = (check(x, y + 1), check(x + 1, y), check(x, y - 1), check(x - 1, y))
    (u, r, d, l) match {
      case (true, true, true, true) => center
      case (true, true, false, false) /* Bottom Left */ => blCorner
      case (true, true, true, false) /* Left */ => vertical
      case (false, true, true, false) /* Upper Left */ => ulCorner
      case (false, true, true, true) /* Top */ => horizontal
      case (false, false, true, true) /* Upper Right */ => urCorner
      case (true, false, true, true) /* Right */ => vertical
      case (true, false, false, true) /* Bottom Right */ => brCorner
      case (true, true, false, true) /* Bottom */ => horizontal
      case _ => '?'
    }
  }

  private[this] def characterPair(b: Board, gem: Gem, x: Int, y: Int) = {
    def check(x: Int, y: Int) = b.at(x, y).exists(_.id == gem.id)
    val (u, r, d, l) = (check(x, y + 1), check(x + 1, y), check(x, y - 1), check(x - 1, y))
    (u, r, d, l) match {
      case (true, true, true, true) => center -> center
      case (true, true, false, false) /* Bottom Left */ => blCorner -> horizontal
      case (true, true, true, false) /* Left */ => vertical -> center
      case (false, true, true, false) /* Upper Left */ => ulCorner -> horizontal
      case (false, true, true, true) /* Top */ => horizontal -> horizontal
      case (false, false, true, true) /* Upper Right */ => horizontal -> urCorner
      case (true, false, true, true) /* Right */ => center -> vertical
      case (true, false, false, true) /* Bottom Right */ => horizontal -> brCorner
      case (true, true, false, true) /* Bottom */ => horizontal -> horizontal
      case _ => '?' -> '?'
    }
  }

  private[this] def getColor(color: Color) = color match {
    case Color.Red => TextColor.ANSI.RED
    case Color.Green => TextColor.ANSI.GREEN
    case Color.Blue => TextColor.ANSI.BLUE
    case Color.Yellow => TextColor.ANSI.YELLOW
    case Color.Wild => TextColor.ANSI.WHITE
  }
}
