package com.leo.xiaoku.store

import com.leo.xiaoku.config.SystemConfig
import com.leo.xiaoku.meta.Tuple
import com.leo.xiaoku.meta.value._
import com.leo.xiaoku.store.fs.{FStore, FileUtils}
import com.leo.xiaoku.store.item.Item
import com.leo.xiaoku.store.page.PagePool
import org.junit.{Before, Test}

import scala.tools.nsc.doc.model.Val

class PageTest {

  @Before
  def cleanFile: Unit = {
    new FStore(SystemConfig.FREEDOM_REL_PATH).cleanFile
  }

  @Test
  def pageTest(): Unit = {

    val values = new Array[Value](7)
    values(0) = new ValueString("this is freedom asdfsadfsadfsadfasdfsaddb")
    values(1) = new ValueString("just ensadfasdfjoy it")
    values(2) = new ValueBoolean(true)
    values(3) = new ValueBoolean(false)
    values(4) = new ValueInt(19999)
    values(5) = new ValueLong(6L)
    values(6) = new ValueLong(1871878777777555555L)
    val tuple = new Tuple(values)
    println(tuple.getLength)
    val item = new Item(tuple)
    val pagePool = PagePool.getInstance()
    val page = pagePool.getFreePage
    import scala.util.control.Breaks._
    page.writeItem(item)
    for (i <- 0 until 100) {
      breakable{
        if (page.writeItem(item)) {
          break()
        } else {
          println("btee=" + i + ",page size exhaust")
        }
      }

    }
    val fStore = new FStore(SystemConfig.FREEDOM_REL_PATH)
    fStore.open()
    fStore.writePageToFile(page, 0)
    val pageLoader = fStore.readPageLoaderFromFile(0)
    val tuples = pageLoader.getTuples
    for (tuple <- tuples) {
      println(tuple)
    }


  }

  @Test
  def readString(): Unit = {
    val fStore = new FStore(SystemConfig.FREEDOM_REL_PATH)
    fStore.open()

  }

  @Test
  def testPageInt(): Unit = {
    val values = new Array[Value](3)
    values(0) = new ValueInt(3)
    values(1) = new ValueInt(4)
    values(2) = new ValueBoolean(true)
    val item = new Item(new Tuple(values))
    val page = PagePool.getInstance().getFreePage
    page.writeItem(item)
    val fStore = new FStore(SystemConfig.FREEDOM_REL_PATH)
    fStore.open()
    fStore.writePageToFile(page, 0)
    val pageLoader = fStore.readPageLoaderFromFile(0)
    val tuples = pageLoader.getTuples
    for (elem <- tuples) {
      print(elem)
    }

  }

}
