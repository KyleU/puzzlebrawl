package services.console

import com.googlecode.lanterna.{TextColor, TextCharacter}

object ConsoleBorders {
  private[this] val ulCorner = '\u2554'
  private[this] val urCorner = '\u2557'
  private[this] val blCorner = '\u255A'
  private[this] val brCorner = '\u255D'
  private[this] val horizontal = '\u2550'
  private[this] val vertical = '\u2551'


  def render(c: ConsoleClient, x: Int, y: Int, width: Int, height: Int, fgColor: TextColor, bgColor: TextColor) = {
    c.screen.setCharacter(x, y, new TextCharacter(ulCorner, fgColor, bgColor))
    c.screen.setCharacter(x + (width * 2) + 1, y, new TextCharacter(urCorner, fgColor, bgColor))
    c.screen.setCharacter(x, y + height + 1, new TextCharacter(blCorner, fgColor, bgColor))
    c.screen.setCharacter(x + (width * 2) + 1, y + height + 1, new TextCharacter(brCorner, fgColor, bgColor))

    c.graphics.drawLine(x + 1, y, x + (width * 2), y, new TextCharacter(horizontal, fgColor, bgColor))
    c.graphics.drawLine(x + 1, y + height + 1, x + (width * 2), y + height + 1, new TextCharacter(horizontal, fgColor, bgColor))

    c.graphics.drawLine(x, y + 1, x, y + height, new TextCharacter(vertical, fgColor, bgColor))
    c.graphics.drawLine(x + (width * 2) + 1, y + 1, x + (width * 2) + 1, y + height, new TextCharacter(vertical, fgColor, bgColor))
  }
}
