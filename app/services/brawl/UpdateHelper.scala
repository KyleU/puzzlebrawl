package services.brawl

import models._
import models.player.Player

import scala.util.Random

trait UpdateHelper { this: BrawlService =>
  private[this] val schedules = brawl.players.flatMap { p =>
    p.script.map {
      case "basic" => UpdateSchedule(p.id, "basic", 500, 2000)
      case "spinner" => UpdateSchedule(p.id, "spinner", 200, 500)
      case "random" => UpdateSchedule(p.id, "random", 500, 2000)
      case x => throw new IllegalStateException("Unhandled")
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

    s.script match {
      case "basic" => self ! BrawlRequest(s.id, basicMove(player))
      case "spinner" => self ! BrawlRequest(s.id, spinnerMove())
      case "random" => self ! BrawlRequest(s.id, randomMove())
      case x => throw new IllegalStateException(s"Unhandled script [$x].")
    }
    schedule(s)
  }

  private[this] def basicMove(player: Player) = {
    ActiveGemsDrop
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
}
