package Extraction

import Extraction.fields.CricketMatch
import Extraction.records.{MostRunsInInningsGenerator, MostRunsRecordGenerator}
import com.typesafe.config.ConfigFactory
import commonutils.HadoopFileSystem
import org.apache.hadoop.fs.FileSystem
import org.apache.spark.SparkContext
import org.apache.spark.sql.{Encoders, SparkSession}
import org.slf4j.LoggerFactory.getLogger
import org.apache.spark.sql.functions.{sum, _}
import org.apache.spark.sql.expressions._
import org.apache.spark.storage.StorageLevel

import scala.collection.mutable

class ExtractionProcessor(sc: SparkSession) {
  @transient lazy val logger = getLogger(this.getClass)
  val config = ConfigFactory.parseResources("env-cricanalysis.conf")
  val userHome = config.getString("cricanalysis.paths.userHome")
  val sourceDirectory = config.getString("cricanalysis.folders.preparation")
  val targetDirectory = config.getString("cricanalysis.folders.extraction")
  val appDirectory = config.getString("cricanalysis.folders.app")
  val dataDirectory = config.getString("cricanalysis.folders.data")
  val calculated = config.getString("cricanalysis.folders.calculated")

  def run() = {
    val conf = sc.sparkContext.hadoopConfiguration
    val fs = FileSystem.get(conf)
    val hadoopFs = new HadoopFileSystem(sc.sparkContext, fs)
    val sourcePath = userHome + appDirectory + sourceDirectory
    val files = hadoopFs.listDirectories(sourcePath)
    val schema = Encoders.product[CricketMatch].schema
    val targetPath = userHome + appDirectory + calculated
    import sc.implicits._
    files.foreach(file => {
      MostRunsRecordGenerator.generateMostRuns(sc, schema, file, targetPath)
      MostRunsInInningsGenerator.generateMostRunsInInnings(sc, schema, file, targetPath)

    })

  }
}
