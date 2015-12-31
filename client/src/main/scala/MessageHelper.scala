import java.util.UUID

import json.{ BaseSerializers, BrawlSerializers, JsonUtils }
import models._
import models.board.mutation.UpdateSegment
import models.brawl.Brawl

import scala.scalajs.js
import scala.util.control.NonFatal

trait MessageHelper { this: PuzzleBrawl =>
  protected[this] def handleMessage(c: String, v: js.Dynamic) = try {
    c match {
      case "GetVersion" => send(VersionResponse("0.0"))
      case "Ping" => send(Pong(JsonUtils.getLong(v.timestamp)))
      case "DebugRequest" => v.data.toString match {
        case "sync" =>
          val json = BrawlSerializers.write(activeBrawl.getOrElse(throw new IllegalStateException()))
          send(DebugResponse("sync", BaseSerializers.write(json)))
        case x if x.startsWith("cheat-") => handleCheat(x.stripPrefix("cheat-"))
        case _ => throw new IllegalArgumentException(s"Unhandled debug request [${v.data.toString}].")
      }

      case "StartBrawl" => handleStartBrawl(v.scenario.toString)

      case "ActiveGemsLeft" => activePlayer.foreach(p => p.activeGemsLeft().foreach { m =>
        send(PlayerUpdate(p.id, Seq(UpdateSegment("active-move", Seq(m)))))
      })
      case "ActiveGemsRight" => activePlayer.foreach(p => p.activeGemsRight().foreach { m =>
        send(PlayerUpdate(p.id, Seq(UpdateSegment("active-move", Seq(m)))))
      })
      case "ActiveGemsClockwise" => activePlayer.foreach(p => p.activeGemsClockwise().foreach { m =>
        send(PlayerUpdate(p.id, Seq(UpdateSegment("active-move", Seq(m)))))
      })
      case "ActiveGemsCounterClockwise" => activePlayer.foreach(p => p.activeGemsCounterClockwise().foreach { m =>
        send(PlayerUpdate(p.id, Seq(UpdateSegment("active-move", Seq(m)))))
      })
      case "ActiveGemsStep" => activePlayer.foreach(p => p.activeGemsStep().foreach { m =>
        send(PlayerUpdate(p.id, Seq(UpdateSegment("active-step", Seq(m)))))
      })
      case "ActiveGemsDrop" => activePlayer.foreach { p =>
        send(PlayerUpdate(p.id, p.dropActiveFullTurn(activeBrawl.getOrElse(throw new IllegalStateException()))))
      }

      case "ResignBrawl" => throw new IllegalStateException("TODO")

      case _ => throw new IllegalStateException(s"Invalid message [$c].")
    }
  } catch {
    case NonFatal(x) => send(ServerError(x.getClass.getSimpleName, x.getMessage))
  }

  protected[this] def handleStartBrawl(scenario: String) = {
    if (scenario != "Offline") {
      throw new IllegalStateException(s"Can't handle scenario [$scenario].")
    }
    val players = Seq(userId -> "Offline User")
    val brawl = Brawl.blank(UUID.randomUUID, "Offline", players = players)
    brawl.setCallbacks(this)
    brawl.players.foreach(_.activeGemsCreate())
    activeBrawl = Some(brawl)
    activePlayer = brawl.players.find(p => p.id == userId)
    send(BrawlJoined(userId, brawl, 0))
  }

  protected[this] def handleCheat(key: String) = key match {
    case "victory" => send(activeBrawl.getOrElse(throw new IllegalStateException()).getCompletionReport)
    case _ => throw new IllegalStateException(s"Unknown cheat [$key].")
  }
}
