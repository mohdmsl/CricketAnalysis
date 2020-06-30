package preparation.Modal

import scala.beans.BeanProperty

class Meta() {
  @BeanProperty var data_version: Double = 0.0
  @BeanProperty var created: String = ""
  @BeanProperty var revision: Int = 0

}
