package com.leo.xiaoku.store.page

import java.util
import java.util.concurrent.atomic.AtomicInteger

class PageNoAllocator() {

  private val count: AtomicInteger = new AtomicInteger(0)

  // todo 线程安全
  private val freePageNoList = new util.LinkedList[Integer]()

  def getNextPageNo: Int = {
    if (freePageNoList.size() == 0) {
      count.getAndAdd(1)
    }
    freePageNoList.remove(0)
  }

  def recycleCount(pageNo: Int): Unit = freePageNoList.add(pageNo)
}
