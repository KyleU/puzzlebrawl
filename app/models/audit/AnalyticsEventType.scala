package models.audit

import enumeratum._

sealed abstract class AnalyticsEventType(val id: String) extends EnumEntry

object AnalyticsEventType extends Enum[AnalyticsEventType] {
  override val values = findValues
  case object ClientTrace extends AnalyticsEventType("trace")
  case object Error extends AnalyticsEventType("error")
  final case class Unknown(override val id: String) extends AnalyticsEventType(id)
}

