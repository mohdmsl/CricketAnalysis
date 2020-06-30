package preparation

import java.io.FileReader

import Extraction.fields.CricketMatch
import com.google.gson.Gson
import com.typesafe.config.ConfigFactory
import commonutils.Utils
import org.apache.spark.sql.SparkSession
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import preparation.processor.{PreparationProcessor, YamlParser}

import scala.collection.mutable

object PreparationLocalApp {

  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().master("local[*]").appName("Cricket Analysis").getOrCreate()

    /*val df = sparkSession.read.text("/home/salik/Downloads/Cricket/all/211028.yaml")
    val data = df.createTempView("oneday")
    val sql = sparkSession.sql("select * from oneday")
    import org.yaml.snakeyaml.representer.Representer
    val representer = new Representer
    representer.getPropertyUtils.setSkipMissingProperties(true)*/


    val sc = sparkSession.sparkContext
    val processor = new PreparationProcessor(sc)
    processor.run


  }
}
