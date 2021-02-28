package com.leo.xiaoku.util

import java.io.RandomAccessFile
import java.nio.ByteBuffer

import org.junit.Test

class BufferWrapperSuit {

  val file = new RandomAccessFile("D:\\workstation\\tmp\\xiaoku\\bw", "rw")

  def saveToFile(bufferWrapper: BufferWrapper): Unit = {
    val channel = file.getChannel
    val buffer = ByteBuffer.wrap(bufferWrapper.getBuffer)
    channel.write(buffer)
  }

  def readFromFile(bufferWrapper: BufferWrapper): Unit = {
    val channel = file.getChannel
    val buffer = ByteBuffer.wrap(bufferWrapper.getBuffer)
    channel.read(buffer)
  }

  @Test
  def testWriteString(): Unit = {
    val bufferWrapper = new BufferWrapper(5)
    bufferWrapper.writeStringWithNull("ABC")
    saveToFile(bufferWrapper)
  }

  @Test
  def testWriteInt(): Unit = {
    val bufferWrapper = new BufferWrapper(size = 7)
    bufferWrapper.writeInt(185)
    saveToFile(bufferWrapper)
  }

  @Test
  def testReadInt(): Unit = {
    val bufferWrapper = new BufferWrapper(7)
    readFromFile(bufferWrapper)
    print(bufferWrapper.readInt())
  }

  @Test
  def intobyte(): Unit = {
    val i = 277772343
    val bufferWrapper = new BufferWrapper(5)
    bufferWrapper.writeInt(i)
    println(bufferWrapper.readIntPos(0))
  }

  @Test
  def readStringWithNull(): Unit = {
    val bufferWrapper = new BufferWrapper(7)
    readFromFile(bufferWrapper)
    bufferWrapper.readStringWithNull
    print(new String(bufferWrapper.getBuffer))
  }

  @Test
  def testWriteReadInt(): Unit = {
    val bufferWrapper = new BufferWrapper(4)
    bufferWrapper.writeInt(789)
    println(bufferWrapper.readInt())
  }
}
