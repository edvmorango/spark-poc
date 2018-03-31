package jobs

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import utils.PathUtils
import domain._

import scala.util.Try

case class NasaRequestJob(sc: SparkContext) {

//  in24.inetnebr.com - - [01/Aug/1995:00:00:01 -0400] "GET /shuttle/missions/sts-68/news/sts-68-mcc-05.txt HTTP/1.0" 200 1839

  def execute() = {

    val raw: RDD[String] = sc.textFile(PathUtils.default)

    val nrs: RDD[NasaRequest] = raw.map { l =>
      if (l.split(' ').size < 4)
        NasaRequest("Ok", "", "", 200, 0)
      else {

        val host = l.split(' ').head

        val timeHead = l.drop(host.size).dropWhile(_ != '[').tail.split(']')

        val time = timeHead.head

        val requestString = timeHead.tail.mkString("")

        val reqClear = requestString.dropWhile(_ != '\"').replace("\"", "")

        val reqList = reqClear.split(" ")

        val reqParts = reqList.takeWhile(s => s.isEmpty || !s.forall(_.isDigit))

        val request = reqParts.mkString(" ")

        val cols = reqList.drop(reqParts.size)

        val code = cols(0).toLong

        val size = Try(cols(1).toLong).getOrElse(0L)

        NasaRequest(host, time, request, code, size)
      }
    }

    nrs

  }

}
