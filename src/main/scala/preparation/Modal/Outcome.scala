package preparation.Modal

import scala.beans.BeanProperty

class Outcome() {
  @BeanProperty var by: By = new By()
  @BeanProperty var  winner: String = ""
  @BeanProperty var  result: String = ""
}
