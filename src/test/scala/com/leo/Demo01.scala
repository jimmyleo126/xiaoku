package com.leo

import java.io.{File, RandomAccessFile}
import java.nio.ByteBuffer

import org.junit.Test

object Demo01 {

  def main(args: Array[String]): Unit = {
    val file = new RandomAccessFile("D:\\workstation\\tmp\\xiaoku\\a", "rw")
    val channel = file.getChannel
    val array = new Array[Byte](45)
    for (i <- 0 to 44) {
      array(i) = i.toByte
    }
    val buffer = ByteBuffer.wrap(array)
   channel.write(buffer, 0)


  }


}
