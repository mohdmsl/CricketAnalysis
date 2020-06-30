package org.example

import java.util.Properties

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.{Logger, LoggerFactory}


object Producer {
  val logger = LoggerFactory.getLogger("CricketLogs")

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.parseResources("kafka-env.conf")
    val topicName = "validation"
    val key = "key1"
    val value = "fuckk you again"
    val props = new Properties()
    val bootstrapServers = config.getString("kafka.Producers.bootstrap-servers")
    props.put("bootstrap.servers", bootstrapServers)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topicName, key, value)

    val p = producer.send(record)
    logger.info("record Send" + p.toString)
    producer.close()

    print("Producer Completed")

  }
}
