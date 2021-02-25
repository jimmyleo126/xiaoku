package com.leo.xiaoku.store.page

import com.leo.xiaoku.store.item.Item
import com.leo.xiaoku.util.BufferWrapper
import jdk.management.resource.internal.ApproverGroup

class Page(defaultSize: Int) {

  protected val pageHeaderData: PageHeaderData = new PageHeaderData(defaultSize)
  protected val bufferWrapper: BufferWrapper = new BufferWrapper(new Array[Byte](defaultSize))
  protected var length: Int = defaultSize
  pageHeaderData.write(this)
  // 是否是脏页，如果是脏页的话，需要写入
  protected var dirty: Boolean = false

  def addTupleCount(page: Page): Unit = {
    pageHeaderData.addTupleCount(page)
  }

  def writeItem(item: Item): Boolean = {
    if (item.writeItem(this)) {
      // 表明此页已脏
      dirty = true
      true
    } else {
      false
    }
  }

  def writeInt(i: Int): Unit = {
    bufferWrapper.writeInt(i)
  }

  /**
    *  在指定位置写入Int
    * @param i 内容
    * @param postition 位置
    */
  def writeIntPos(i: Int, postition: Int): Unit = {
    bufferWrapper.writeIntPos(i, postition)
  }

  def writeStringWithNull(s: String): Unit ={
    bufferWrapper.writeStringWithNull(s)
  }

  def writeBytes(src: Array[Byte], position: Int): Unit = {
    bufferWrapper.writeBytes(src, position)
  }

  def readInt: Int = bufferWrapper.readInt()

  def readIntPos(position: Int): Int = bufferWrapper.readIntPos(position)

  def readStringWithNull: String = bufferWrapper.readStringWithNull

  def readBytes(position: Int, length: Int): Array[Byte] = bufferWrapper.readBytes(position, length)

  /**
    * 剩余多少freeSpace
    * @return
    */
  def remainFreeSpace: Int = pageHeaderData.getUpperOffset - pageHeaderData.getLowerOffset

  def getUpperOffset: Int = pageHeaderData.getUpperOffset

  def getLowerOffset: Int = pageHeaderData.getLowerOffset

  def modifyLowerOffset(i: Int): Unit = {
    pageHeaderData.modifyLowerOffset(i, this)
  }

  def modifyUpperOffset(i: Int): Unit = {
    pageHeaderData.modifyUpperOffset(i, this)
  }

  def getBuffer: Array[Byte] = bufferWrapper.getBuffer

  def getLength: Int = length

}
