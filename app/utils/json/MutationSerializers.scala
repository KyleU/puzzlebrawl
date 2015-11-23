package utils.json

import models.game.board.mutation.Mutation
import models.game.board.mutation.Mutation.{ RemoveGem, ChangeGem, MoveGem, AddGem }
import play.api.libs.json._

object MutationSerializers {
  import utils.json.GameSerializers.{ gemReads, gemWrites }

  implicit val addGemReads = Json.reads[AddGem]
  implicit val addGemWrites = Json.writes[AddGem]

  implicit val moveGemReads = Json.reads[MoveGem]
  implicit val moveGemWrites = Json.writes[MoveGem]

  implicit val changeGemReads = Json.reads[ChangeGem]
  implicit val changeGemWrites = Json.writes[ChangeGem]

  implicit val removeGemReads = Json.reads[RemoveGem]
  implicit val removeGemWrites = Json.writes[RemoveGem]

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
      }
      JsObject(Seq("t" -> JsString(v._1), "v" -> v._2))
    }
  }
}
