package app

import config.SparkConfBuilder
import jobs.NasaRequestJob
import org.apache.spark.SparkContext

object Main {

  def main(args: Array[String]): Unit = {

    val sc = new SparkContext(SparkConfBuilder.defaultConfiguration)
    println(sc.defaultMinPartitions)

    val job = NasaRequestJob(sc)

    job.execute()

  }

}
