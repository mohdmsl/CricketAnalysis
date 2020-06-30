package preparation.Modal

import scala.beans.BeanProperty

class Toss() {
  @BeanProperty var decision: String = ""
  @BeanProperty var winner: String = ""
}
