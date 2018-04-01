package utils

import java.time.format.DateTimeFormatter

object DateFormatter {
  import java.time.ZonedDateTime

  private val default = "dd/MMM/yyyy:HH:mm:ss Z"

  def format(date: String, format: String = default): ZonedDateTime = {
    val fmt = DateTimeFormatter.ofPattern(default)

    ZonedDateTime.parse("01/Aug/1995:00:00:01 -0400", fmt)
  }
}
