package preparation.Modal

import java.util

import Extraction.fields.Batting

import scala.beans.BeanProperty

 class CricketMatch() {
  @BeanProperty var meta: Meta = new Meta()
  @BeanProperty var info: Info = new Info()
  @BeanProperty var innings = new util.ArrayList[util.HashMap[String,Batting]]()
}
