package com.leo.xiaoku.index.bp

import com.leo.xiaoku.index.BaseIndex
import com.leo.xiaoku.meta.{Attribute, Relation, Tuple}

class BPTree(
              relation: Relation,
              indexName: String,
              attributes: Array[Attribute])
  extends BaseIndex(relation: Relation, indexName: String, attributes: Array[Attribute]){

  // 根节点
  protected var root: BPNode = _

  // 叶子节点的链表头
  protected var head: BPNode = _

  protected var nodeMap: Map[Int, BPNode] = _


  override def get(key: Tuple): Unit = ???

  override def remove(key: Tuple): Unit = ???

  override def insert(key: Tuple): Unit = root.insert(key, this)

  override def flushToDisk(): Unit = {
    // 深度遍历

  }

  def getHead: BPNode = head

  def setHead(head: BPNode): BPTree = {
    this.head = head
    this
  }
}
