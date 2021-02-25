package com.leo.xiaoku.store.item

import com.leo.xiaoku.meta.Tuple
import com.leo.xiaoku.store.page.Page

/**
  *
  * @param tuple 元组
  */
class Item(tuple: Tuple) {

  private val data: ItemData = new ItemData(tuple)
  private val ptr: ItemPointer = new ItemPointer(0, data.getLength)

  /**
    *  写入item，如果空间不够，返回false
    * @param page
    * @return
    */
  def writeItem(page: Page): Boolean = {
    val freeSpace = page.remainFreeSpace
    if (freeSpace < getLength) {
      return false
    }
     // 顺序必须如此，只有写入data之后
    // 才能知道ptr中的offset
    data.write(page)
    // 修正ptr中的offset
    ptr.setOffset(data.getOffset)
    ptr.write(page)
    page.addTupleCount(page)
    true
  }

  def getLength: Int = data.getLength + ptr.getPtrLength


}

/**
  * ItemData
  * 包装tuple,从而能够和page进行交互
  *
  * @Author lizhuyang
  */
class ItemData(tuple: Tuple) {

  // Item实际存储的offset
  private var offset = 0
  // Item实际的长度
  private var length = 0

  length = tuple.getLength

  def write(page: Page) = {
    // 获取从长度
    val tupleLength = length
    // 找到写入的位置
    val writePosition = page.getUpperOffset - tupleLength
    // 写入数据
    page.writeBytes(tuple.getBytes, writePosition)
    // 更新upperOffset
    page.modifyUpperOffset(writePosition)
    // 更新ItemData的offset，length
    offset = writePosition
  }

  def getOffset: Int = offset

  def getLength: Int = length
}

/**
  *
  * @param offset Tuple的偏移
  * @param length Tuple的长度
  */
class ItemPointer(var offset: Int, val tupleLength: Int) {

//  private val tupleLength: Int = length

  def write(page: Page): Unit = {
    page.writeInt(offset)
    page.writeInt(tupleLength)
    // 修改freespace的lowerOffset
    var lowerOffset = page.getLowerOffset
    lowerOffset += getPtrLength
    page.modifyLowerOffset(lowerOffset)
  }

  def setOffset(offset: Int): ItemPointer = {
    this.offset = offset
    this
  }

  def getPtrLength: Int = 8

  def getOffset: Int = offset

  def getTupleLength: Int = tupleLength
}
