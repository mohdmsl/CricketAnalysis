package Extraction

import org.apache.spark.sql.SparkSession
import preparation.processor.PreparationProcessor

object ExtractionLocalApp {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().master("local[*]").appName("Cricket Analysis").getOrCreate()
    val sc = sparkSession.sparkContext
    val processor = new ExtractionProcessor(sparkSession)
    processor.run
  }
}
