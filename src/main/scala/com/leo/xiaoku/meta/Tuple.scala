package com.leo.xiaoku.meta

import com.leo.xiaoku.meta.value.Value
import com.leo.xiaoku.util.BufferWrapper

import scala.collection.mutable

class Tuple(values: Array[Value]) {

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

  }

  def getLength: Int = {
    var sum = 0
    for (value <- values) {
      sum += value.getLength
    }
    sum
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
