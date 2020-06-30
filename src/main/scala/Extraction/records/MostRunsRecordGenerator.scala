package Extraction.records

import Extraction.fields.CricketMatch
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, count, explode, max, row_number, sum, when}
import org.apache.spark.sql.types.StructType
import org.apache.spark.storage.StorageLevel
import org.slf4j.LoggerFactory.getLogger

object MostRunsRecordGenerator {
  @transient lazy val logger = getLogger(this.getClass)
  def generateMostRuns(sc:SparkSession, schema: StructType, file: String) = {
    val df = sc.read.schema(schema).json(file.replace("file:", "")).as[CricketMatch]
    logger.info("reading file " + file)
    val deliveryDF = df.withColumn("matchID", row_number().over(Window.orderBy("info")))
      .select(col("matchID"), explode(col("innings")).as("innings"))
      .select(col("matchID"), explode(col("innings")))
      .select(col("matchID"), col("key"), col("value.team"), col("value.deliveries"))
      .select(col("matchID"), col("key"), col("team"), explode(col("deliveries")).as("ball"))
      .select(col("matchID"), col("key").as("inning"), col("team"), explode(col("ball")))
      .persist(StorageLevel.MEMORY_AND_DISK)

    val mostRunsDF = deliveryDF.groupBy(col("value.batsman"), col("team"))
      .agg(sum("value.runs.batsman").as("runs"), count("value.batsman").as("ballsFaced"), count(when(col("value.runs.batsman") === 4, true)).as("4s"), count(when(col("value.runs.batsman") === 6, true)).as("6s"))
      .select(col("batsman"), col("team"), col("runs"), col("runs")./(col("ballsFaced")).*(100).as("SR"), col("4s"), col("6s"))
      .orderBy(col("runs").desc)

    val matchesPlayedByBatsman = deliveryDF.groupBy(col("matchID"), col("value.batsman"))
      .agg(count(col("value.batsman")), sum(col("value.runs.batsman")).as("score"), count(when(col("value.wicket.player_out") === col("value.batsman"), true)).as("OUT"))
      .groupBy(col("batsman"))
      .agg(count(col("batsman")).as("matches"), max("score").as("HS"), count(when(col("score") >= 50, true)).as("50"), count(when(col("score") >= 100, true)).as("100"),
        count(when(col("score") === 0, true)).as("0"), (count(col("batsman")) - sum(col("OUT"))).as("NO"))

    val mostRunsInCareerDf = mostRunsDF.join(matchesPlayedByBatsman, "batsman").withColumn("average", col("runs") / (col("matches") - col("NO")))


    val targetPath = userHome + appDirectory + calculated + "mostRuns/" + System.currentTimeMillis()
    logger.info("writing to output path: " + targetPath)
    mostRunsInCareerDf.write.json(targetPath)
    logger.info("file saved successfully")
  }
}
