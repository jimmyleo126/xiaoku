package com.leo.xiaoku.index

import com.leo.xiaoku.index.bp.BPTree
import com.leo.xiaoku.meta.Tuple
import com.leo.xiaoku.meta.value.{Value, ValueInt, ValueString}
import org.junit.Test

import scala.util.Random

class BPPageTest {

  @Test
  def testWriteBpIndex(): Unit = {
    val bPTree = new BPTree(null, "bpindex", null)
    val insertSize = 40000
    for (i <- 1 to insertSize) {
      val random = new Random()
      val toInsert = random.nextInt(insertSize)
      val tuple = genTuple(toInsert)
      bPTree.insert(tuple)
    }
    bPTree.flushToDisk()
  }

  def genTuple(i: Int): Tuple = {
    val values = new Array[Value](2)
    values(0) = new ValueInt(i)
    val random = new Random()
    var str = ""
    val strSize = random.nextInt(20) + 1
    for (j <- 0 until strSize) {
      str += random.nextInt()
    }
    if (str.length > 40) {
      str = str.substring(0, 40)
    }
    values(1) = new ValueString(str)
    new Tuple(values)
  }
}
