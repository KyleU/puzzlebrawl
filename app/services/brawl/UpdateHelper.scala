package services.brawl

import models._

import scala.util.Random

trait UpdateHelper { this: BrawlService =>
  private[this] val schedules = brawl.players.flatMap { p =>
    p.script.map {
      case "basic" => UpdateSchedule(p.id, "basic", 500, 2000)
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
    println(delay)
    context.system.scheduler.scheduleOnce(delay.milliseconds, self, s)
  }

  protected[this] def handleUpdateSchedule(s: UpdateSchedule) = {
    s.script match {
      case "basic" =>
        val m = Random.nextInt(10) match {
          case i if i >= 0 && i <= 2 => ActiveGemsLeft
          case i if i >= 3 && i <= 5 => ActiveGemsRight
          case i if i >= 6 && i <= 6 => ActiveGemsClockwise
          case i if i >= 7 && i <= 7 => ActiveGemsCounterClockwise
          case i if i >= 8 && i <= 10 => ActiveGemsDrop
        }
        self ! BrawlRequest(s.id, m)
      case x => throw new IllegalStateException(s"Unhandled script [$x].")
    }
    schedule(s)
  }
}
