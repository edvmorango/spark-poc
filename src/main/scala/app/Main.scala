package app

import config.SparkConfBuilder
import org.apache.spark.{SparkConf, SparkContext}

object Main {

  def main(args: Array[String]): Unit = {

    val sc = new SparkContext(SparkConfBuilder.defaultConnection())

  }

}
