package models.board.mutation

case class UpdateSegment(
  mutations: Seq[Mutation],
  combo: Option[Int] = None,
  scoreDelta: Option[Int] = None
)
