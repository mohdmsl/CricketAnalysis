package Extraction.records

import Extraction.fields.CricketMatch
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.storage.StorageLevel
import org.slf4j.LoggerFactory.getLogger

object MostRunsInInningsGenerator {
  @transient lazy val logger = getLogger(this.getClass)

  def generateMostRunsInInnings(sc: SparkSession, schema: StructType, file: String, targetPath: String) = {
    import sc.implicits._
    val df = sc.read.schema(schema).json(file.replace("file:", "")).as[CricketMatch]
    logger.info("reading file " + file)
    val deliveryDF = df.withColumn("matchID", row_number().over(Window.orderBy("info")))
      .select(col("matchID"), col("info.venue"), col("info.teams"), col("info.dates"), explode(col("innings")).as("innings"))
      .select(col("matchID"), col("venue"), col("teams"), col("dates"), explode(col("innings")))
      .select(col("matchID"), col("venue"), col("teams"), col("dates"), col("key"), col("value.team"), explode(col("value.deliveries")).as("ball"))
      .select(col("matchID"), col("venue"), col("teams"), col("dates"), col("key").as("inning"), col("team"), explode(col("ball")))
      .persist(StorageLevel.MEMORY_AND_DISK)

    val additionaRecordData = deliveryDF.groupBy(col("matchID"), col("venue"), col("teams"), col("dates")).agg(count("key").as("key"))

    val mostRunsDF = deliveryDF.groupBy(col("matchID"), col("value.batsman"), col("team"))
      .agg((sum("value.runs.batsman") + sum(when(col("value.extras.noballs").isNotNull, col("value.extras.noballs") - 1).otherwise(0)) + sum(when(col("value.extras.byes").isNotNull, col("value.extras.byes")).otherwise(0))).as("runs")
        , count(when(col("value.extras.wides").isNull, true)).as("ballsFaced"), count(when(col("value.runs.batsman") === 4, true)).as("4s"), count(when(col("value.runs.batsman") === 6, true)).as("6s"))
      .select(col("matchID"), col("batsman"), col("team"), col("runs"), col("ballsFaced"), round((col("runs")./(col("ballsFaced")).*(100)), 2).as("SR"), col("4s"), col("6s"))
      .orderBy(col("runs").desc)

    // scores of batsman in a match i.e. scorecard
    /*val runsScoredPerMatchDF = deliveryDF.groupBy( col("value.batsman"))
      .agg(sum(col("value.runs.batsman"), count("value.batsman").as("ballsFaced")).as("score")).persist(StorageLevel.MEMORY_AND_DISK)
*/
    // highest score of a batsman
    val highestScoreDF = mostRunsDF
      .groupBy(col("batsman"))
      .agg(max("runs").as("runs"))

    val mostRunsInCareerDf = highestScoreDF.join(mostRunsDF, Seq("batsman", "runs")).join(additionaRecordData, "matchID").drop("key")

    logger.info("writing to output path: " + targetPath)
    mostRunsInCareerDf.write.json(targetPath + "mostRunsInInnings/" + System.currentTimeMillis())
    logger.info("file saved successfully")
  }
}
