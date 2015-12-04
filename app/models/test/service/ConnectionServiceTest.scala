package models.test.service

import akka.testkit.TestProbe
import controllers.admin.TestController
import models.{ VersionResponse, GetVersion, Pong, Ping }
import models.user.User
import services.connection.ConnectionService
import services.supervisor.ActorSupervisor
import utils.{ DateUtils, Logging }

object ConnectionServiceTest extends Logging {
  def testConnection() = {
    implicit val system = play.api.Play.current.actorSystem
    val testProbe = TestProbe()

    val initStart = DateUtils.nowMillis
    val conn = system.actorOf(ConnectionService.props(None, ActorSupervisor.instance, User.mock, testProbe.ref))
    val initMs = DateUtils.nowMillis - initStart

    val runStart = DateUtils.nowMillis

    conn ! Ping(DateUtils.nowMillis)
    testProbe.expectMsgClass(classOf[Pong])

    conn ! GetVersion
    testProbe.expectMsgClass(classOf[VersionResponse])

    val runMs = DateUtils.nowMillis - runStart

    TestController.Result("connection", "ok", Seq.empty, initMs.toInt, runMs.toInt)
  }
}
