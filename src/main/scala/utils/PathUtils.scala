package utils

object PathUtils {

  lazy val base = System.getProperty("user.dir")
  lazy val default = base.concat("/datasets")
  lazy val result = base.concat("/result/")
}
