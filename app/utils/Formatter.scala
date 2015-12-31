package utils

import org.apache.commons.lang3.StringUtils
import org.joda.time.{ LocalDateTime, LocalTime, LocalDate }

object Formatter {
  private[this] val numFormatter = java.text.NumberFormat.getNumberInstance(java.util.Locale.US)

  def withCommas(i: Int) = numFormatter.format(i.toLong)
  def withCommas(l: Long) = numFormatter.format(l)
  def withCommas(d: Double) = numFormatter.format(d)
  def padLeft(s: String, numDigits: Int, padChar: Char = ' ') = StringUtils.leftPad(s, numDigits, padChar)
  def niceDate(d: LocalDate) = d.toString("EEEE, MMM dd, yyyy")
  def niceTime(d: LocalTime) = d.toString("HH:mm:ss")
  def niceDateTime(dt: LocalDateTime) = s"${niceDate(dt.toLocalDate)} ${niceTime(dt.toLocalTime)} UTC"

  def className(instance: Any) = instance.getClass.getSimpleName.stripSuffix("$")
}
