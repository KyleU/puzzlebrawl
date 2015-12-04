package models.test.service

import java.util.UUID

import akka.testkit.TestProbe
import controllers.admin.TestController
import models.user.{ PlayerRecord, User }
import models._
import services.brawl.BrawlService
import services.connection.ConnectionService
import services.supervisor.ActorSupervisor
import utils.{ DateUtils, Logging }

object BrawlServiceTest extends Logging {
  def testBrawl() = {
    implicit val system = play.api.Play.current.actorSystem
    val testProbe = TestProbe()

    val initStart = DateUtils.nowMillis
    val connId = UUID.randomUUID
    val conn = system.actorOf(ConnectionService.props(Some(connId), ActorSupervisor.instance, User.mock, testProbe.ref))
    val playerRecords = Seq(PlayerRecord(User.mock.id, "Test User", Some(connId), Some(conn)))
    val brawl = system.actorOf(BrawlService.props(UUID.randomUUID, "scenario", playerRecords, 0))
    val initMs = DateUtils.nowMillis - initStart

    val runStart = DateUtils.nowMillis

    conn ! ActiveGemsLeft
    val update1 = testProbe.expectMsgClass(classOf[PlayerUpdate])
    assert(update1.segments.size == 1)

    conn ! ActiveGemsRight
    val update2 = testProbe.expectMsgClass(classOf[PlayerUpdate])
    assert(update2.segments.size == 1)

    conn ! ActiveGemsDrop
    testProbe.expectMsgClass(classOf[PlayerUpdate])

    val runMs = DateUtils.nowMillis - runStart

    TestController.Result("brawl", "ok", Seq.empty, initMs.toInt, runMs.toInt)
  }
}
