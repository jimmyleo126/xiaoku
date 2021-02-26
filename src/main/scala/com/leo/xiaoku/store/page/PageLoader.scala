package com.leo.xiaoku.store.page

import com.leo.xiaoku.meta.Tuple
import com.leo.xiaoku.store.item.ItemPointer

class PageLoader(page: Page) {

  private var tuples: Array[Tuple] = _
  private var tupleCount: Int = _

  def load(): Unit = {
    val pageHeaderData = PageHeaderData.read(page)
    tupleCount = pageHeaderData.getTupleCount
    var ptrStartOff = pageHeaderData.getLength
    // 首先建立存储tuple数组
    tuples = new Array[Tuple](tupleCount)
    // 循环读取
    for (i <- 0 until tupleCount) {
      // 重新从page读取tuple
      val ptr = new ItemPointer(page.readInt, page.readInt)
      val bb = page.readBytes(ptr.getOffset, ptr.getTupleLength)
      val tuple = new Tuple
      tuple.read(bb)
      tuples(i) = tuple
      // 进入到下一个元组的位置
      ptrStartOff += ptr.getTupleLength
    }
  }

  def getTuples: Array[Tuple] = tuples

  def getTupleCount: Int = tupleCount
}
