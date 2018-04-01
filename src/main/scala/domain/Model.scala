package domain

import java.time.ZonedDateTime

case class NasaRequest(host: String,
                       time: ZonedDateTime,
                       request: String,
                       code: Long,
                       bytes: Long)
    extends Serializable
