package models.board.mutation

case class UpdateSegment(
  category: String,
  mutations: Seq[Mutation],
  combo: Option[Int] = None,
  charge: Option[Double] = None,
  scoreDelta: Option[Int] = None)
