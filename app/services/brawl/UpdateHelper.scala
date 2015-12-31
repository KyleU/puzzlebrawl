package services.brawl

import models._
import models.player.Player

import scala.util.Random

trait UpdateHelper { this: BrawlService =>
  private[this] val schedules = brawl.players.flatMap { p =>
    p.script.map {
      case "basic" => UpdateSchedule(p.id, "basic", 1000, 2000)
      case "spinner" => UpdateSchedule(p.id, "spinner", 200, 300)
      case "random" => UpdateSchedule(p.id, "random", 1000, 2000)
      case "simple" => UpdateSchedule(p.id, "simple", 2000, 2000)
      case "speedy" => UpdateSchedule(p.id, "speedy", 50, 100)
      case x => throw new IllegalStateException(s"Unhandled schedule [$x].")
    }
  }

  def startSchedules() = {
    schedules.foreach { s =>
      schedule(s)
    }
  }

  def schedule(s: UpdateSchedule) = {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    import scala.concurrent.duration._
    val delay = s.minActionMs + (Random.nextFloat * (s.maxActionMs - s.minActionMs)).toInt
    context.system.scheduler.scheduleOnce(delay.milliseconds, self, s)
  }

  protected[this] def handleUpdateSchedule(s: UpdateSchedule) = {
    val player = brawl.playersById(s.id)

    if (player.status == "active") {
      s.script match {
        case "basic" => self ! BrawlRequest(s.id, basicMove(player))
        case "spinner" => self ! BrawlRequest(s.id, spinnerMove())
        case "random" => self ! BrawlRequest(s.id, randomMove())
        case "simple" => self ! BrawlRequest(s.id, simpleMove())
        case "speedy" => self ! BrawlRequest(s.id, simpleMove())
        case x => throw new IllegalStateException(s"Unhandled script [$x].")
      }
      schedule(s)
    }
  }

  private[this] def basicMove(player: Player) = {
    val ag1 = player.activeGems.headOption.getOrElse(throw new IllegalStateException())
    val ag2 = player.activeGems.tail.headOption.getOrElse(throw new IllegalStateException())

    if (ag1.y == ag2.y + 1) {
      ActiveGemsClockwise
    } else {
      firstOccupiedIndex(player, ag1.x, ag1.y) match {
        case Some(idx) =>
          val g = player.board.at(ag1.x, idx).getOrElse(throw new IllegalStateException())
          if (g.color == ag1.gem.color) {
            ActiveGemsDrop
          } else {
            Random.nextInt(5) match {
              case x if x == 0 || x == 1 => ActiveGemsLeft
              case x if x == 2 || x == 3 => ActiveGemsRight
              case x if x == 4 => ActiveGemsDrop
            }
          }
        case None => ActiveGemsDrop
      }
    }
  }

  private[this] def spinnerMove() = if (Random.nextInt(10) == 0) {
    ActiveGemsDrop
  } else {
    ActiveGemsClockwise
  }

  private[this] def randomMove() = Random.nextInt(10) match {
    case i if i >= 0 && i <= 2 => ActiveGemsLeft
    case i if i >= 3 && i <= 5 => ActiveGemsRight
    case i if i >= 6 && i <= 6 => ActiveGemsClockwise
    case i if i >= 7 && i <= 7 => ActiveGemsCounterClockwise
    case i if i >= 8 && i <= 10 => ActiveGemsDrop
  }

  private[this] def simpleMove() = {
    ActiveGemsDrop
  }

  private[this] def firstOccupiedIndex(player: Player, x: Int, y: Int) = {
    (0 until player.board.height).reverseIterator.drop(player.board.height - y).find(i => player.board.at(x, i).isDefined)
  }
}
