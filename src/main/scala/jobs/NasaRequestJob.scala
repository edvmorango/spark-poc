package jobs

import java.time.{LocalDateTime}

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import utils.{DateFormatter, PathUtils}
import domain._

import scala.util.Try

case class NasaRequestJob(sc: SparkContext) {

  private def getData(): RDD[NasaRequest] = {

    val raw: RDD[String] = sc.textFile(PathUtils.default)

    val data: RDD[NasaRequest] = raw.flatMap { l =>
      if (l.split(' ').size < 4)
        Nil
      else {

        val host = l.split(' ').head

        val timeHead = l.drop(host.size).dropWhile(_ != '[').tail.split(']')

        val time = DateFormatter.format(timeHead.head)

        val requestString = timeHead.tail.mkString("")

        val reqClear = requestString.dropWhile(_ != '\"').replace("\"", "")

        val reqList = reqClear.split(" ")

        val reqParts = reqList.takeWhile(s => s.isEmpty || !s.forall(_.isDigit))

        val request = reqParts.mkString(" ")

        val cols = reqList.drop(reqParts.size)

        val code = cols(0).toLong

        val size = Try(cols(1).toLong).getOrElse(0L)

        Array(NasaRequest(host, time, request, code, size))
      }

    }

    data.cache()

  }

  def execute() = {

    val responses = getData()

    val uniqueHosts = responses.map(_.host).distinct().count()

    val nfRequests = responses.filter(_.code == 404).cache()

    val countNfRequests = nfRequests.count()

    val bytes = responses.map(_.bytes).reduce(_ + _)

    val mostErroneousUrls =
      nfRequests
        .map(r => (r.host, 1L))
        .reduceByKey(_ + _)
        .top(5)(Ordering.fromLessThan[(String, Long)]((f, s) => s._2 > f._2))

    val outputPath = PathUtils.result.concat(LocalDateTime.now().toString)

    nfRequests
      .map(r => (r.time.toLocalDate, 1L))
      .reduceByKey(_ + _)
      .saveAsTextFile(outputPath)

    println(s"Unique Valid Hosts: $uniqueHosts")
    println(s"404 Ocurrency: $countNfRequests")
    println(s"404 most frequent URLs ${mostErroneousUrls.toList}")
    println(s"Total of Bytes: $bytes")
    println(s"Errors per Day path: $outputPath")
  }

}
