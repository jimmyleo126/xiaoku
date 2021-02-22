package com.leo.xiaoku.store.page

import java.util.concurrent.ConcurrentLinkedDeque

class PagePool {

  // 可用page
  private val frees = new ConcurrentLinkedDeque[Page]()

  // page工厂
  private val factory = PageFactory.getInstance

  def init(): Unit = {
    // 初始化8页的数据
    for (i <- 0 until PagePool.defaultPageNum) {
      frees.add(factory.newPage)
    }
  }

  def getFreePage: Page = {
    factory.newPage
  }

}

object PagePool {

  private val pagePool: PagePool = new PagePool

  def getInstance(): PagePool = pagePool

  // 默认页数
  private var defaultPageNum = 8


}
