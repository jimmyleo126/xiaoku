package com.leo.xiaoku.store.fs

import java.io.{EOFException, RandomAccessFile}
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

object FileUtils {

  def open(fileName: String): FileChannel = {
    try {
      val file = new RandomAccessFile(fileName, "rw")
      file.getChannel
    }catch {
      case e: Exception =>
        throw new RuntimeException(e)
    }

  }

  def writeFully(channel: FileChannel, src: ByteBuffer, position: Int): Unit = {
    if (channel.position() != position) {
      channel.position(position)
    }
    do {
      channel.write(src)
    } while (src.remaining() > 0)
  }

  def readFully(channel: FileChannel, dst: ByteBuffer, position: Long): Unit = {
    if (channel.position() != position) {
      channel.position(position)
    }
    do {
      val r = channel.read(dst)
      if (r < 0) {
        throw new EOFException()
      }
    } while (dst.remaining() > 0)
  }

}
