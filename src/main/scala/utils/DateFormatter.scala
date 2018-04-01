package utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateFormatter {
  import java.time.ZonedDateTime

  private val default = "dd/MMM/yyyy:HH:mm:ss Z"

  private val dtDefault = "dd/MM/yyyy"

  def format(date: String, format: String = default): ZonedDateTime = {
    val fmt = DateTimeFormatter.ofPattern(format)

    ZonedDateTime.parse(date, fmt)
  }

  def zonedToLocalDate(date: ZonedDateTime,
                       format: String = dtDefault): LocalDate = {
    LocalDate.parse(date.toLocalDate.toString)
  }

}
