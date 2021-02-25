package com.leo.xiaoku.store.page

import com.leo.xiaoku.config.SystemConfig

class PageHeaderData(size: Int) {

  // todo 变的时候 需要同步更新这里
  val PAGE_HEADER_SIZE = 24
  // Page开头的magicWorld
  var magicWord = "Freedom"
  // free space的起始偏移
  var lowerOffset: Int = _
  // 指向pageHeader中的lowerOffset起始位置
  val LOWER_POINTER: Int = 8
  // free space的最终偏移
  var upperOffset: Int = _
  // 指向pageHeader中的upperOffset起始位置
  val UPPER_POINTER = 12
  // special space的起始偏移
  var special: Int = _
  // 指向pageHeader中的special起始位置
  val SPECIAL_POINTER = 16
  // 记录元组的数量
  var tupleCount: Int = _
  val TUPLE_COUNT_POINTER = 20

  // 记录header的长度
  var headerLength: Int = _

  val magicWorldLength: Int = magicWord.getBytes().length + 1
  lowerOffset = magicWorldLength + 4 + 4 + 4 + 4
  // 给special 64字节的空间
  upperOffset = size - SystemConfig.DEFAULT_SPECIAL_POINT_LENGTH
  special = upperOffset
  headerLength = lowerOffset

  def modifyLowerOffset(i: Int, page: Page): Unit = {
    lowerOffset = i
    // 修改byte数组位置
    page.writeIntPos(i, LOWER_POINTER)
  }

  def modifyUpperOffset(i: Int, page: Page): Unit = {
    upperOffset = i
    // 修改byte数组的位置
    page.writeIntPos(i, UPPER_POINTER)
  }

  def getUpperOffset: Int = upperOffset

  def getLowerOffset: Int = lowerOffset

  def addTupleCount(page: Page): Unit = {
    var count = page.readIntPos(TUPLE_COUNT_POINTER)
    count += 1
    // 修改tuple的数量
    page.writeIntPos(count, TUPLE_COUNT_POINTER)
    tupleCount = count
  }

  def write(page: Page): Unit = {
    page.writeStringWithNull(magicWord)
    page.writeInt(lowerOffset)
    page.writeInt(upperOffset)
    page.writeInt(special)
    page.writeInt(tupleCount)
  }

  def getTupleCount: Int = tupleCount

  def getLength: Int = headerLength
}

object PageHeaderData {

  def read(page: Page): PageHeaderData = {
    val pageHeaderData = new PageHeaderData(page.getLength)
    pageHeaderData.magicWord = page.readStringWithNull
    pageHeaderData.lowerOffset = page.readInt
    pageHeaderData.upperOffset = page.readInt
    pageHeaderData.special = page.readInt
    pageHeaderData.tupleCount = page.readInt
    pageHeaderData
  }
}
