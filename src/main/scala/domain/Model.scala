package domain


object NasaRequest {

//  def apply(): NasaRequest = new NasaRequest(host, time, request, code, bytes)
  
}

case class NasaRequest(host: String, time: String, request: String, code: Long, bytes: Long)
