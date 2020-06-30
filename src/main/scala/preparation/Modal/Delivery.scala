package preparation.Modal

import scala.beans.BeanProperty

class Delivery() {
  @BeanProperty var batsman: String = ""
  @BeanProperty var bowler: String = ""
  @BeanProperty var non_striker: String = ""
  @BeanProperty var runs: Runs = new Runs()
  @BeanProperty var extras = new Extras()
}
