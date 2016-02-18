package models.audit

import org.joda.time.{ LocalDate, LocalDateTime }

final case class DailyMetricResult(date: LocalDate, metric: DailyMetric, value: Long, measured: LocalDateTime)
