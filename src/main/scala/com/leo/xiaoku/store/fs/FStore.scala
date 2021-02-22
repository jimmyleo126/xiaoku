package com.leo.xiaoku.store.fs

import java.nio.ByteBuffer
import java.nio.channels.FileChannel

import com.leo.xiaoku.config.SystemConfig
import com.leo.xiaoku.store.page.Page

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



}
