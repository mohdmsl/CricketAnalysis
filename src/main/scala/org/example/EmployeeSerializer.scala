package org.example

import org.apache.kafka.common.serialization.Serializer

class EmployeeSerializer extends Serializer[Employee]{
  override def serialize(s: String, t: Employee): Array[Byte] = ???
}
