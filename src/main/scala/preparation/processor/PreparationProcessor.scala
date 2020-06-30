package preparation.processor

import java.io.OutputStreamWriter

import Extraction.fields.CricketMatch
import com.typesafe.config.ConfigFactory
import commonutils.{HadoopFileSystem, Utils}
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkContext
import org.slf4j.LoggerFactory.getLogger

import scala.collection.mutable.ListBuffer

class PreparationProcessor(sc: SparkContext) {

  @transient lazy val logger = getLogger(this.getClass)
  val config = ConfigFactory.parseResources("env-cricanalysis.conf")
  val path = config.getString("cricanalysis.paths.userHome")
  val preparationDirectory = config.getString("cricanalysis.folders.preparation")
  val appDirectory = config.getString("cricanalysis.folders.app")
  val dataDirectory = config.getString("cricanalysis.folders.data")

  def run(): Unit = {
    val conf = sc.hadoopConfiguration
    val fs = FileSystem.get(conf)
    val hadoopFs = new HadoopFileSystem(sc, fs)
    val files = hadoopFs.listFiles(path + dataDirectory + "/t20s")
    val list = ListBuffer.empty[String]
    files.foreach(file => {
      logger.info("parsing file" + file)
      val matchData = YamlParser.parseYamlFile(file)

      matchData match {
        case Some(data) => {
          list += Utils.getAsJson(data)
        }
        case None => None
      }

    })
    val json = list.mkString("\n")
    val outputPath = path + appDirectory + preparationDirectory + System.currentTimeMillis + "/prepared"
    logger.info("prepared file" + outputPath)
    hadoopFs.writeToHdfs(json, outputPath, ".json")


  }
}
