package jobs

import java.time.ZonedDateTime

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import utils.{DateFormatter, PathUtils}
import domain._

import scala.collection.TraversableOnce
import scala.util.Try

case class NasaRequestJob(sc: SparkContext) {

  private def transformData(l: String): NasaRequest = {
    if (l.split(' ').size < 4)
      NasaRequest("Ok", ZonedDateTime.now(), "", 200, 0)
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

      NasaRequest(host, time, request, code, size)
    }
  }

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

    val nfRequestsCount = nfRequests.count()

    val mostErroneousUrls =
      nfRequests
        .map(r => (r.host, 1L))
        .reduceByKey(_ + _)
        .top(5)(Ordering.fromLessThan[(String, Long)]((f, s) => s._2 > f._2))

    val errorsPerDay =
      nfRequests
        .map(r => (r.time.toLocalDate, 1L))
        .reduceByKey(_ + _)
        .collect()
        .toList

    val bytes = responses.map(_.bytes).reduce(_ + _)

    println(s"Hosts: $uniqueHosts" +
      s"\n404 Count: $nfRequestsCount\n404 Urls: ${mostErroneousUrls.toList}\nErrors per day: ${errorsPerDay}" +
      s"\nBytes: $bytes")

  }

}
