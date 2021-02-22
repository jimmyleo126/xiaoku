package com.leo.xiaoku.meta.value

import com.leo.xiaoku.util.BufferWrapper

private[xiaoku] abstract class Value {

  val UNKNOWN: Byte = 100
  val STRING: Byte = 1
  val INT: Byte = 2
  val LONG: Byte = 3
  val BOOLEAN: Byte = 4

  def getLength: Int

  def getType: Byte

  def getBytes: Array[Byte]

  def read(bytes: Array[Byte])

  def compare(value: Value): Int

}

class ValueBoolean(var b: Boolean) extends Value {

//  var b: Boolean = _


  override def getLength: Int = {
    // 1 for type
    1 + 1
  }

  override def getType: Byte = BOOLEAN

  override def getBytes: Array[Byte] = {
    val result = new Array[Byte](2)
    result(0) = BOOLEAN
    if (b) {
      result(1) = 1
    } else {
      result(1) = 0
    }
    result
  }

  override def read(bytes: Array[Byte]): Unit = {
    if (bytes(0) == 0) {
      b = false
    } else {
      b = true
    }
  }

  override def compare(value: Value): Int = {
    0
  }
}

class ValueInt(var i: Int) extends Value {
  override def getLength: Int = 1 + 4

  override def getType: Byte = INT

  override def getBytes: Array[Byte] = {
    val wrapper = new BufferWrapper(getLength)
    // int type
    wrapper.writeByte(INT)
    // for the value string
    wrapper.writeInt(i)
    wrapper.getBuffer

  }

  override def read(bytes: Array[Byte]): Unit = {
    val wrapper = new BufferWrapper(bytes)
    i = wrapper.readInt()
  }

  override def compare(value: Value): Int = {
    val toCompare: Int = value.asInstanceOf[ValueInt].getInt
    toCompare match {
      case x if i > x => 1
      case x if i == x => 0
      case _ => -1
    }
  }

  def getInt: Int = i
}

class ValueLong(var l: Long) extends Value {
  override def getLength: Int = 1 + 8

  override def getType: Byte = LONG

  override def getBytes: Array[Byte] = {
    val wrapper = new BufferWrapper(getLength)
    // long type
    wrapper.writeByte(LONG)
    // for the value string
    wrapper.writeLong(l)
    wrapper.getBuffer
  }

  override def read(bytes: Array[Byte]): Unit = {
    val wrapper = new BufferWrapper(bytes)
    l = wrapper.readLong
  }

  override def compare(value: Value): Int = {
    val toCompare = value.asInstanceOf[ValueLong].getLong
    toCompare match {
      case x if l > toCompare => 1
      case x if 1 == toCompare => 0
      case _ => -1
    }
  }

  def getLong: Long = l
}

class ValueString(var s: String) extends Value {

//  private val s: String = _

  def this() = this(null)

  override def getLength: Int = 1 + 4 + s.length

  override def getType: Byte = STRING

  override def getBytes: Array[Byte] = {
    val wrapper = new BufferWrapper(getLength)
    wrapper.writeByte(getType)
    // 此处写入的是string的长度
    wrapper.writeStringLength(s)
    wrapper.getBuffer
  }

  override def read(bytes: Array[Byte]): Unit = {
    s = new String(bytes)
  }

  override def compare(value: Value): Int = {
    0
  }

  def getString: String = s

  def setString(s: String): ValueString = {
    this.s = s
    this
  }
}
