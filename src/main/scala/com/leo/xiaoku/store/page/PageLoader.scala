package com.leo.xiaoku.store.page

import com.leo.xiaoku.meta.Tuple

class PageLoader(page: Page) {

  private val tuples: Array[Tuple] = null
  private var tupleCount: Int = _

  def load: Unit = {
    val pageHeaderData = PageHeaderData.read(page)
    tupleCount = pageHeaderData.getTupleCount

  }


}
