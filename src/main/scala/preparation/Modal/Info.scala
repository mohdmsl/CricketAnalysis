package preparation.Modal

import scala.beans.BeanProperty

class Info() {
  @BeanProperty var city: String = ""
  @BeanProperty var  dates = new java.util.ArrayList[String]()
  @BeanProperty var  gender: String = ""
  @BeanProperty var  match_type: String = ""
  @BeanProperty var  outcome: Outcome = new Outcome()
  @BeanProperty var  overs: Int = 0
  @BeanProperty var  player_of_match = new java.util.ArrayList[String]()
  @BeanProperty var  teams = new java.util.ArrayList[String]()
  @BeanProperty var  toss: Toss = new Toss
  @BeanProperty var  umpires = new java.util.ArrayList[String]()
  @BeanProperty var  venue: String = ""
}