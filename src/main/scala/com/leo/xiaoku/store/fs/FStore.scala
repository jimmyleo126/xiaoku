package com.leo.xiaoku.store.fs

import java.nio.ByteBuffer
import java.nio.channels.FileChannel

import com.leo.xiaoku.config.SystemConfig
import com.leo.xiaoku.index.bp.{BPPage, BPTree}
import com.leo.xiaoku.store.page.{Page, PageLoader, PagePool}

class FStore(filePath: String) {

  // 文件路径
//  private var filePath: String = _
  // 文件channel
  private var fileChannel: FileChannel = _
  // 当前filePosition
  private var currentFilePosition: Long = 0

  def open(): Unit = {
    fileChannel = FileUtils.open(filePath)
  }

  def  writePageToFile(page: Page, pageIndex: Int): Unit = {
    val writePos = pageIndex * SystemConfig.DEFAULT_PAGE_SIZE
    val byteBuffer = ByteBuffer.wrap(page.getBuffer)
    FileUtils.writeFully(fileChannel, byteBuffer, writePos)
  }

  def readPageFromFile(pageIndex: Int): Page = {
    readPageFromFile(pageIndex, false, null)
  }

  def readPageFromFile(pageIndex: Int, isIndex: Boolean, bPTree: BPTree): Page = {
    val readPos = pageIndex * SystemConfig.DEFAULT_PAGE_SIZE
    val buffer = ByteBuffer.allocate(SystemConfig.DEFAULT_PAGE_SIZE)
    try {
      FileUtils.readFully(fileChannel, buffer, readPos)
    } catch {
      case e: Exception =>
        null
    }
    // byteBuffer 转 buffer
    val b = new Array[Byte](SystemConfig.DEFAULT_PAGE_SIZE)
    // position跳回原始位置
    buffer.flip()
    buffer.get(b)
    if (!isIndex) {
      // 从池中拿取空页
      val page = PagePool.getInstance().getFreePage
      // 初始化page
      page.read(b)
      page
    } else {
      val bPPage = new BPPage(SystemConfig.DEFAULT_PAGE_SIZE)
      bPPage.read(b)
      bPPage
    }
  }

  def readPageLoaderFromFile(pageIndex: Int): PageLoader = {
    val page = readPageFromFile(pageIndex)
    val pageLoader = new PageLoader(page)
    // 装载byte
    pageLoader.load
    pageLoader
  }



}
