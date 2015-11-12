package models.game.board

trait BoardHelper
  extends MutationHelper
  with CollapseHelper
  with CrashHelper
  with FuseHelper { this: Board =>

}
