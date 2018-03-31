package app

import config.SparkConfBuilder
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import utils.PathUtils

object Main {

  def main(args: Array[String]): Unit = {

    val sc = new SparkContext(SparkConfBuilder.defaultConfiguration)

    val rdd: RDD[String] = sc.textFile(PathUtils.default)

    println(rdd.count())

  }

}
