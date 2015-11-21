package services.console

import java.util.UUID

import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.{ TerminalPosition, TextCharacter, TextColor }
import models.game.{ Player, Game }
import models.game.test.TextGemPattern
import org.joda.time.LocalDateTime
import utils.{ DateUtils, Formatter }

class ConsoleClient(game: Game) {
  private[this] var activePlayer: Option[UUID] = None

  def setActivePlayer(id: UUID) = {
    activePlayer = Some(id)
    addStatusLog(s"Player [$id] selected.")
  }

  val screen = {
    val factory = new DefaultTerminalFactory()
    factory.setSuppressSwingTerminalFrame(true)
    val terminal = factory.createTerminal()
    val s = new TerminalScreen(terminal)
    s.startScreen()
    s.clear()
    s
  }

  val graphics = screen.newTextGraphics()

  private val rows = screen.getTerminalSize.getRows
  private val cols = screen.getTerminalSize.getColumns

  var playerLocations = Seq.empty[(Player, Int, Int)]
  game.players.foreach(p => add(p))

  def stop() = screen.stopScreen()

  private var nextBoardX = 0
  private var nextBoardY = 0

  private[this] def add(p: Player) = {
    if (nextBoardX + (p.board.width * 2) + 3 > cols) {
      nextBoardX = 0
      val currentY = nextBoardY
      nextBoardY += playerLocations.filter(_._3 == currentY).map(_._1.board.height + 3).max
    }
    playerLocations = playerLocations :+ ((p, nextBoardX, nextBoardY))
    nextBoardX += (p.board.width * 2) + 3
  }

  private[this] val statusLogs = collection.mutable.ListBuffer.empty[(LocalDateTime, String)]
  private[this] val statusRows = rows - 15
  private[this] def writeStatus() = {
    graphics.setForegroundColor(TextColor.ANSI.WHITE)
    val logs = statusLogs.zipWithIndex.takeRight(statusRows).reverse
    for(l <- logs.zipWithIndex) {
      val logIndex = l._1._2
      val rowIndex = l._2
      val log = l._1._1
      val msg = "[" + Formatter.niceTime(log._1.toLocalTime) + "] (" + (logIndex + 1) + "/" + statusLogs.size + "): " + log._2
      graphics.putString(0, rows - rowIndex - 1, msg + (0 until cols).map(x => " ").mkString(""))
    }
    render()
  }

  def addStatusLog(s: String) = {
    statusLogs += (DateUtils.now -> s)
    writeStatus()
  }

  def render() = {
    if (game.players.isEmpty) {
      throw new IllegalStateException("No players in client.")
    }

    playerLocations.foreach { b =>
      val fg = if(activePlayer.contains(b._1.id)) { TextColor.ANSI.CYAN } else { TextColor.ANSI.WHITE }
      ConsoleBorders.render(this, b._2, b._3, b._1, fg, TextColor.ANSI.BLACK)
      (0 until b._1.board.height).foreach { y =>
        (0 until b._1.board.width).foreach { x =>
          val pattern = TextGemPattern.pair(b._1.board, x, y)
          val targetX = b._2 + 1 + (x * 2)
          val targetY = b._3 + 1 + (b._1.board.height - y - 1)

          screen.setCharacter(targetX, targetY, new TextCharacter(pattern._1, pattern._3, TextColor.ANSI.BLACK))
          screen.setCharacter(targetX + 1, targetY, new TextCharacter(pattern._2, pattern._3, TextColor.ANSI.BLACK))
        }
      }
    }

    screen.setCursorPosition(new TerminalPosition(cols - 1, rows - 1))
    screen.refresh()
  }
}
