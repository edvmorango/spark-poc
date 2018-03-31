package config

import org.apache.spark.SparkConf

object SparkConfBuilder {

  private val defConfig =
    Map( "spark.app.name" -> "spark-poc",
      "spark.master" -> "local",
      "spark.driver.memory" -> "2g"
    ).toTraversable


  def defaultConnection(): SparkConf = {
    new SparkConf().setAll(defConfig)
  }




}
