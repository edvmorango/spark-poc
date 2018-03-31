package app

import config.SparkConfBuilder
import jobs.NasaRequestJob
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import utils.PathUtils

object Main {

  def main(args: Array[String]): Unit = {

    val sc = new SparkContext(SparkConfBuilder.defaultConfiguration)

    val job = NasaRequestJob(sc)

    val x = job.execute()

    println(x.count())

    println("FINISHED")
  }

}
