package utils.json

import models.board.mutation.{ UpdateSegment, Mutation }
import models.board.mutation.Mutation._
import play.api.libs.json._

object MutationSerializers {
  import utils.json.BrawlSerializers.{ gemReads, gemWrites, gemLocationReads, gemLocationWrites }

  implicit val addGemReads = Json.reads[AddGem]
  implicit val addGemWrites = Json.writes[AddGem]

  implicit val moveGemReads = Json.reads[MoveGem]
  implicit val moveGemWrites = Json.writes[MoveGem]

  implicit val changeGemReads = Json.reads[ChangeGem]
  implicit val changeGemWrites = Json.writes[ChangeGem]

  implicit val removeGemReads = Json.reads[RemoveGem]
  implicit val removeGemWrites = Json.writes[RemoveGem]

  implicit val activeGemsUpdateReads = Json.reads[ActiveGemsUpdate]
  implicit val activeGemsUpdateWrites = Json.writes[ActiveGemsUpdate]

  implicit val mutationReads = new Reads[Mutation] {
    override def reads(json: JsValue) = {
      val v = (json \ "v").as[JsObject]
      (json \ "t").as[String] match {
        case "a" => addGemReads.reads(v)
        case "m" => moveGemReads.reads(v)
        case "c" => changeGemReads.reads(v)
        case "r" => removeGemReads.reads(v)
        case t => throw new IllegalArgumentException(s"Type [$t] is not a valid mutation.")
      }
    }
  }
  implicit val mutationWrites = new Writes[Mutation] {
    override def writes(m: Mutation) = {
      val v = m match {
        case ag: AddGem => "a" -> addGemWrites.writes(ag)
        case mg: MoveGem => "m" -> moveGemWrites.writes(mg)
        case cg: ChangeGem => "c" -> changeGemWrites.writes(cg)
        case rg: RemoveGem => "r" -> removeGemWrites.writes(rg)
        case agu: ActiveGemsUpdate => "g" -> activeGemsUpdateWrites.writes(agu)
      }
      JsObject(Seq("t" -> JsString(v._1), "v" -> v._2))
    }
  }

  implicit val updateSegmentReads = Json.reads[UpdateSegment]
  implicit val updateSegmentWrites = Json.writes[UpdateSegment]
}
