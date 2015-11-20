package models.game.board

trait BoardHelper
  extends CollapseHelper
  with CrashHelper
  with DropHelper
  with FuseHelper
  with TimerHelper
  with WildHelper { this: Board =>

  def fullTurn() = {
    val fuseActions = fuse()

    val wildsActions = processWilds()
    val postWildFuseActions = if(wildsActions.isEmpty) {
      Seq.empty
    } else {
      fuse()
    }

    val crashActions = crash()
    val postCrashFuseActions = if(crashActions.isEmpty) {
      Seq.empty
    } else {
      fuse()
    }

    val collapseActions = collapse()
    val postCollapseFuseActions = if(collapseActions.isEmpty) {
      Seq.empty
    } else {
      fuse()
    }

    Seq(fuseActions, wildsActions, postWildFuseActions, crashActions, postCrashFuseActions, Seq(collapseActions), postCollapseFuseActions).flatten
  }
}
