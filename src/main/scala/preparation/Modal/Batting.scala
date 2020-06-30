package preparation.Modal

import java.util

import scala.beans.BeanProperty
import scala.collection.mutable

class Batting() {
  @BeanProperty var team: String = ""
  @BeanProperty var deliveries = new util.ArrayList[util.HashMap[Double,Delivery]]()
}
