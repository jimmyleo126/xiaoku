package com.leo.xiaoku.util

import com.leo.xiaoku.internal.Logging

import scala.util.control.Breaks

private[xiaoku] class BufferWrapper(private val buffer: Array[Byte]) extends Logging{

  // 写索引
  private var writeIndex: Int = 0
  // 读索引
  private var readIndex: Int = 0

  private var length: Int = buffer.length

  def this(size: Int) = this(new Array[Byte](size))

  def getBuffer: Array[Byte] = buffer

  def writeInt(i: Int): Unit = {
    buffer({writeIndex += 1; writeIndex - 1}) = (i & 0xff).asInstanceOf[Byte]
    buffer({writeIndex += 1; writeIndex - 1}) = (i >>> 8).asInstanceOf[Byte]
    buffer({writeIndex += 1; writeIndex - 1}) = (i >>> 16).asInstanceOf[Byte]
    buffer({writeIndex += 1; writeIndex - 1}) = (i >>> 24).asInstanceOf[Byte]
  }

  def writeIntPos(i: Int, position: Int):Unit = {
    var p = position
    buffer({ p += 1; p - 1 }) = (i & 0xff).asInstanceOf[Byte]
    buffer({ p += 1; p - 1 }) = (i >>> 8).asInstanceOf[Byte]
    buffer({ p += 1; p - 1 }) = (i >>> 16).asInstanceOf[Byte]
    buffer({ p += 1; p - 1 }) = (i >>> 24).asInstanceOf[Byte]
  }

  def writeByte(i: Byte): Unit = buffer({writeIndex += 1; writeIndex - 1}) = i

  def writeLong(l: Long): Unit = {
    buffer({writeIndex += 1; writeIndex - 1}) = (l & 0xff).asInstanceOf[Byte]
    buffer({writeIndex += 1; writeIndex - 1}) = (l >>> 8 ).asInstanceOf[Byte]
    buffer({writeIndex += 1; writeIndex - 1}) = (l >>> 16 ).asInstanceOf[Byte]
    buffer({writeIndex += 1; writeIndex - 1}) = (l >>> 24 ).asInstanceOf[Byte]
    buffer({writeIndex += 1; writeIndex - 1}) = (l >>> 32 ).asInstanceOf[Byte]
    buffer({writeIndex += 1; writeIndex - 1}) = (l >>> 40 ).asInstanceOf[Byte]
    buffer({writeIndex += 1; writeIndex - 1}) = (l >>> 48 ).asInstanceOf[Byte]
    buffer({writeIndex += 1; writeIndex - 1}) = (l >>> 56 ).asInstanceOf[Byte]
  }

  def writeBytes(src: Array[Byte]): Unit = {
    System.arraycopy(src, 0, buffer, writeIndex, src.length)
  }

  def writeBytes(src: Array[Byte], position: Int): Unit = {
    System.arraycopy(src, 0, buffer, position, src.length)
  }

  def writeStringLength(s: String): Unit = {
    writeStringLength(s.getBytes)
  }

  def writeStringLength(src: Array[Byte]): Unit = {
    writeInt(src.length)
    writeBytes(src)
  }

  def writeStringWithNull(s: String): Unit = {
    writeWithNull(s.getBytes)
  }

  def writeWithNull(src: Array[Byte]): Unit = {
    System.arraycopy(src, 0, buffer, writeIndex, src.length)
    writeIndex += src.length
    // 写入0
    buffer(writeIndex) = 0
    writeIndex += 1
  }

  def readByte: Byte = buffer({readIndex += 1; readIndex - 1})

  def readInt(): Int = {
    val b = this.buffer
    var i = b({ readIndex += 1; readIndex - 1 }) & 0xff
    i |= (b({ readIndex += 1; readIndex - 1 }) & 0xff) << 8
    i |= (b({ readIndex += 1; readIndex - 1 }) & 0xff) << 16
    i |= (b({ readIndex += 1; readIndex - 1 }) & 0xff) << 24
    i
  }

  def readIntPos(position: Int): Int = {
    var p = position
    val b = this.buffer
    var i = b({ p += 1; p - 1 }) & 0xff
    i |= (b({ p += 1; p - 1 }) & 0xff) << 8
    i |= (b({ p += 1; p - 1 }) & 0xff) << 16
    i |= (b({ p += 1; p - 1 }) & 0xff) << 24
    i
  }

  def readLong: Long = {
    val b = this.buffer
    var l = (b({ readIndex += 1; readIndex - 1 }) & 0xff).toLong
    l |= (b({ readIndex += 1; readIndex - 1 }) & 0xff).toLong << 8
    l |= (b({readIndex += 1; readIndex - 1}) & 0xff).toLong << 16
    l |= (b({readIndex += 1; readIndex - 1}) & 0xff).toLong << 24
    l |= (b({readIndex += 1; readIndex - 1}) & 0xff).toLong << 32
    l |= (b({readIndex += 1; readIndex - 1}) & 0xff).toLong << 40
    l |= (b({readIndex += 1; readIndex - 1}) & 0xff).toLong << 48
    l |= (b({readIndex += 1; readIndex - 1}) & 0xff).toLong << 56
    l
  }

  def readStringWithNull: String = {
    new String(readBytesWithNull)
  }

  def readBytesWithNull: Array[Byte] = {
    val b = this.buffer
    if (readIndex >= length) {
      null
    }
    var offset = -1
    // todo 可以用scala Array index 日后来改 待测试
    offset = b.indexOf(0, readIndex)
    /*val loop = new Breaks
    loop.breakable{
      for (i <- readIndex until length) {
        if (b(i) == 0) {
          offset = i
          loop.break()
        }
      }
    }*/
    offset match {
      case -1 =>
        val ab1 = new Array[Byte](length - readIndex)
        System.arraycopy(b, readIndex, ab1, 0, ab1.length)
        readIndex = length
        ab1
      case 0 =>
        readIndex += 1
        null
      case _ =>
        val ab2 = new Array[Byte](offset - readIndex)
        System.arraycopy(b, readIndex, ab2, 0, ab2.length)
        readIndex = offset + 1
        ab2
    }

  }

  def readBytes(length: Int): Array[Byte] = {
    val b = this.buffer
    val result = new Array[Byte](length)
    // 拷贝原数据到新的Byte中
    System.arraycopy(b, readIndex, result, 0, length)
    readIndex += length
    result
  }

  /**
    *
    * @param position 指定位置
    * @param length 目标长度
    * @return
    */
  def readBytes(position: Int, length: Int):Array[Byte] = {
    val b = this.buffer
    // todo 可以scala 数组切分API val k = b.slice(position, length)
    val result = new Array[Byte](length)
    // 拷贝原数据到新的byte中
    System.arraycopy(b, position, result, 0, length)
    result
  }

  def remaining: Int = length - readIndex
}
