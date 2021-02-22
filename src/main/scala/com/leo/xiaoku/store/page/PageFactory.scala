package com.leo.xiaoku.store.page

import com.leo.xiaoku.config.SystemConfig
import com.leo.xiaoku.index.bp.{BPNode, BPPage}


class PageFactory {

  def newPage: Page = new Page(SystemConfig.DEFAULT_PAGE_SIZE)

  def newBpPage(bpNode: BPNode): BPPage = new BPPage(bpNode)

}
object PageFactory {

  private val factory = new PageFactory

  def getInstance: PageFactory = factory


}
