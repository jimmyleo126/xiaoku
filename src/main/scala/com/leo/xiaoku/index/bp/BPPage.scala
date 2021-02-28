package com.leo.xiaoku.index.bp

import com.leo.xiaoku.config.SystemConfig
import com.leo.xiaoku.constant.ItemConst
import com.leo.xiaoku.store.item.Item
import com.leo.xiaoku.store.page.Page

class BPPage(defaultSize: Int) extends Page(defaultSize){

  private var bPNode: BPNode = _

  def this(bPNode: BPNode) {
    this(SystemConfig.DEFAULT_PAGE_SIZE)
    this.bPNode = bPNode
  }

  def getInitFreeSpace: Int = {
    val size: Int = if (bPNode.isLeaf) {
      7
    } else {
      6
    }
    length - SystemConfig.DEFAULT_SPECIAL_POINT_LENGTH - ItemConst.INT_LENGTH * size - pageHeaderData.PAGE_HEADER_SIZE
  }

  def getContentSize: Int = {
    var size = 0
    bPNode.getEntries.foreach(size += Item.getItemLength(_))
    if (!bPNode.isLeaf) {
      for (elem <- bPNode.getEntries) {
        size += ItemConst.INT_LENGTH
      }
      size += ItemConst.INT_LENGTH
    }
    size
  }

  def cacluateRemainFreeSpace: Int = getInitFreeSpace - getContentSize


}
