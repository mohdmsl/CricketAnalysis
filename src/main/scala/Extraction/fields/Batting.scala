package Extraction.fields

import scala.collection.mutable

case class Batting(
                    team: String,
                    deliveries: List[mutable.Map[String, Delivery]]
                  )
