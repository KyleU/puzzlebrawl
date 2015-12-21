package services.sandbox

import utils.{ ApplicationContext, Logging }

import scala.concurrent.Future

trait SandboxTask extends Logging {
  def id: String
  def description: String
  def run(ctx: ApplicationContext): Future[String]
}
