package com.leo.xiaoku.index.bp

import com.leo.xiaoku.config.SystemConfig
import com.leo.xiaoku.store.page.Page

class BPPage(defaultSize: Int) extends Page(defaultSize){

  private var bPNode: BPNode = _

  def this(bPNode: BPNode) {
    this(SystemConfig.DEFAULT_PAGE_SIZE)
    this.bPNode = bPNode
  }


}
