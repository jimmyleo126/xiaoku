package com.leo.xiaoku.meta

import java.util

import com.leo.xiaoku.meta.value._
import com.leo.xiaoku.util.BufferWrapper

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Tuple(var values: Array[Value]) {

  // 元组中的值
//   private var values: Array[Value] = _

  def this() =  this(null)

  // 获取其byte
  def getBytes: Array[Byte] = {
    val bb = new Array[Byte](getLength)
    var postition = 0
    for (value <- values) {
      System.arraycopy(value.getBytes, 0, bb, postition, value.getLength)
      postition += value.getLength
    }
    bb
  }

  def read(bytes: Array[Byte]): Unit = {
    val wrapper = new BufferWrapper(bytes)
    var result = ListBuffer[Value]()
    while (wrapper.remaining > 0) {
      // 获取类型
      val ty = wrapper.readByte
      var bs: Array[Byte] = null
      var value: Value = null
      ty match {
        case Value.STRING =>
          // 获取长度
          val length = wrapper.readInt()
          bs = wrapper.readBytes(length)
          value = new ValueString()
        case Value.BOOLEAN =>
          bs = wrapper.readBytes(1)
          value = new ValueBoolean()
        case Value.INT =>
          bs = wrapper.readBytes(4)
          value = new ValueInt()
        case Value.LONG =>
          bs = wrapper.readBytes(8)
          value = new ValueLong()
        case _ =>
          throw new RuntimeException("value type match error")
      }
      value.read(bs)
      result += value
    }
    values = result.toArray
  }

  def getLength: Int = {
    var sum = 0
    for (value <- values) {
      sum += value.getLength
    }
    sum
  }

  /**
    * 和另外一个tuple比较
    * 注意，另一个tuple可能是索引，所以两者column的length可能不等
    * @param tuple
    * @return
    */
  def compare(tuple: Tuple): Int = {
    val min = if (values.length < tuple.getValues.length) values.length else tuple.getValues.length
    for (i <- 0 until min) {
      val comp = values(i).compare(tuple.getValues(i))
      if (comp != 0) return comp
    }
    0
  }

  def getValues: Array[Value] = values

  override def toString = {
    var k = ""
    for (elem <- values) {
      k += elem
    }
    k
  }

}

class TupleDesc(attrs: Array[Attribute]) {

  // 元组的属性数组
//  private var attrs: Array[Attribute] = _
  private var attrsMap: mutable.HashMap[String, Attribute] = new mutable.HashMap[String, Attribute]()
  for (attr <- attrs) {
    attrsMap.put(attr.name, attr)
  }

  def getItems(): Unit = {

  }

}

case class Attribute(
    // 属性名称
    name: String,
    // 属性类型
    type1: Int,
    // 在TupleDesc中的位置
    index: Int,
    // 注释
    comment: String)
