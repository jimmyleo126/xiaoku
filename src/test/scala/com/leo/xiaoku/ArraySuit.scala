package com.leo.xiaoku

import org.junit.Test

class ArraySuit {

  @Test
  def testArray(): Unit = {

    val b = Array.range(1, 7)
    val k = b.slice(2, 4)
    print(b)
  }
}
