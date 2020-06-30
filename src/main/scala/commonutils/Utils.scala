package commonutils

import com.google.gson.Gson

object Utils {

  def getAsJson[T](data:T): String = {
    val gson = new Gson()
    gson.toJson(data)
  }

}
