import utils.Logging

object Sandbox extends Logging {
  def main(args: Array[String]) {
    log.info(s"Sandbox Task started with arguments [${args.mkString(", ")}].")
  }
}
