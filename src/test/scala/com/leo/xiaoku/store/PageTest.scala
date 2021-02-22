package com.leo.xiaoku.store

import com.leo.xiaoku.config.SystemConfig
import com.leo.xiaoku.meta.Tuple
import com.leo.xiaoku.meta.value._
import com.leo.xiaoku.store.fs.FStore
import com.leo.xiaoku.store.item.Item
import com.leo.xiaoku.store.page.PagePool
import org.junit.Test

import scala.tools.nsc.doc.model.Val

class PageTest {

  @Test
  def pageTest(): Unit = {

    val values = new Array[Value](5)
    values(0) = new ValueString("this is freedom db")
    values(1) = new ValueString("just enjoy it")
    values(2) = new ValueBoolean(true)
    values(3) = new ValueInt(5)
    values(4) = new ValueLong(6L)
    val tuple = new Tuple(values)
    val item = new Item(tuple)
    val pagePool = PagePool.getInstance()
    val page = pagePool.getFreePage
    import scala.util.control.Breaks._
    page.writeItem(item)
//    for (i <- 0 until 1) {
//      breakable{
//        if (page.writeItem(item)) {
//          break()
//        } else {
//          println("btee=" + i + ",page size exhaust")
//        }
//      }
//
//    }
    val fStore = new FStore(SystemConfig.FREEDOM_REL_PATH)
    fStore.open()
    fStore.writePageToFile(page, 0)


  }
}
