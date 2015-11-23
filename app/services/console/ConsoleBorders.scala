package services.console

import com.googlecode.lanterna.{ TextColor, TextCharacter }
import models.game.player.Player
import utils.Formatter

object ConsoleBorders {
  val ulCorner = '\u2554'
  val urCorner = '\u2557'
  val blCorner = '\u255A'
  val brCorner = '\u255D'
  val horizontal = '\u2550'
  val vertical = '\u2551'
  val topT = '\u2566'
  val bottomT = '\u2569'
  val center = ' '

  def render(c: ConsoleClient, x: Int, y: Int, player: Player, fgColor: TextColor, bgColor: TextColor): Unit = {
    val width = player.board.width
    val height = player.board.height

    c.graphics.setForegroundColor(fgColor)
    c.graphics.setBackgroundColor(bgColor)

    c.screen.setCharacter(x, y, new TextCharacter(ulCorner, fgColor, bgColor))

    c.screen.setCharacter(x + (width * 2) + 1, y, new TextCharacter(topT, fgColor, bgColor))
    c.screen.setCharacter(x + (width * 2) + 2, y, new TextCharacter(horizontal, fgColor, bgColor))
    c.screen.setCharacter(x + (width * 2) + 3, y, new TextCharacter(horizontal, fgColor, bgColor))
    c.screen.setCharacter(x + (width * 2) + 4, y, new TextCharacter(urCorner, fgColor, bgColor))

    c.screen.setCharacter(x, y + height + 1, new TextCharacter(blCorner, fgColor, bgColor))

    c.screen.setCharacter(x + (width * 2) + 1, y + height + 1, new TextCharacter(bottomT, fgColor, bgColor))
    c.screen.setCharacter(x + (width * 2) + 2, y + height + 1, new TextCharacter(horizontal, fgColor, bgColor))
    c.screen.setCharacter(x + (width * 2) + 3, y + height + 1, new TextCharacter(horizontal, fgColor, bgColor))
    c.screen.setCharacter(x + (width * 2) + 4, y + height + 1, new TextCharacter(brCorner, fgColor, bgColor))

    val top = {
      val l = player.name.length
      val padding = (((width * 2) - l) / 2) - 2
      val ret = (0 to padding).map(idx => horizontal).mkString + "[" + player.name + "]" + (0 to padding).map(idx => horizontal).mkString
      if (ret.length > width * 2) { ret.substring(0, width * 2) } else { ret }
    }
    c.graphics.putString(x + 1, y, top)

    val bottom = {
      val padding = (((width * 2) - 6) / 2) - 2
      val paddedScore = Formatter.padLeft(player.score.toString, 6, '0')
      val ret = (0 to padding).map(idx => horizontal).mkString + "[" + paddedScore + "]" + (0 to padding).map(idx => horizontal).mkString
      if (ret.length > width * 2) { ret.substring(0, width * 2) } else { ret }
    }
    c.graphics.putString(x + 1, y + height + 1, bottom)

    c.graphics.drawLine(x, y + 1, x, y + height, vertical)
    c.graphics.drawLine(x + (width * 2) + 1, y + 1, x + (width * 2) + 1, y + height, vertical)
    c.graphics.drawLine(x + (width * 2) + 4, y + 1, x + (width * 2) + 4, y + height, vertical)
  }
}
