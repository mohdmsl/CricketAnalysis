package commonutils

import java.io.OutputStreamWriter

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkContext
import org.slf4j.LoggerFactory.getLogger

import scala.collection.mutable.ListBuffer

class HadoopFileSystem(sc: SparkContext, fs: FileSystem) {

  @transient lazy val logger = getLogger(this.getClass)

  def listFiles(path: String): Array[String] = {
    fs.listStatus(new Path(path)).map(_.getPath.toString.replace("file:", ""))

  }

  def writeToHdfs(json: String, path: String, extension: String) = {
    val outputPath = new Path(path + extension)
    val out = new OutputStreamWriter(fs.create(outputPath, false))
    out.write(json)
    logger.info("file written succesfully")
    out.flush()
    out.close()
  }

  def listDirectories(path: String) = {
    val iterator = fs.listFiles(new Path(path), true)
    val directories = ListBuffer.empty[String]
    while (iterator.hasNext) {
      directories += iterator.next().getPath.toString
    }
    directories
  }


}
