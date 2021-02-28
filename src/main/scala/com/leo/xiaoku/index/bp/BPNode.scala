package com.leo.xiaoku.index.bp

import com.leo.xiaoku.constant.ItemConst
import com.leo.xiaoku.meta.Tuple
import com.leo.xiaoku.store.item.Item
import com.leo.xiaoku.store.page.PageFactory

import scala.collection.mutable.ListBuffer

class BPNode(
            _isLeaf: Boolean, // 是否为叶子节点
            isRoot: Boolean, // 是否为根节点
            bPTree: BPTree   // 隶属于哪个bpTree
            ) {

  // 对应的pageNo
  protected val pageNo: Int = bPTree.getNextPageNo
  // 父节点
  protected var parent: BPNode = _
  // 叶节点的前节点
  protected var previous: BPNode = _
  // 叶节点的后节点
  protected var next: BPNode = _
  // 节点的关键字
  protected val entries: ListBuffer[Tuple] =  ListBuffer[Tuple]()
  // 子节点
  protected var children: ListBuffer[BPNode] = _
  // 页结构
  protected val bPPage: BPPage = PageFactory.getInstance.newBpPage(this)

  if (!isLeaf) {
    children = new ListBuffer[BPNode]()
  }

  def this(isLeaf: Boolean, bPTree: BPTree) = this(isLeaf, false, bPTree)

  /**
    * 插入到当前节点的关键字中
    * 有序
    * @param key
    */
  protected def innerInsert(key: Tuple): Unit = {
    // 如果关键字列表长度为0，则直接插入
    if (entries.isEmpty) {
      entries += key
      return
    }
    // 否则遍历列表
    for (i <- entries.indices) {
      // 如果该关键字键值已存在，则更新
      // todo duplicated key error
      if (entries(i).compare(key) == 0) {
        return
      } else if (entries(i).compare(key) > 0) {
        entries.insert(i, key)
        return
      }
    }
    // 插入到末尾
    entries += key
  }

  def insert(key: Tuple, tree: BPTree): Unit = {
    if (getBorrowKeyLength(key) > bPPage.getInitFreeSpace / 3) {
      throw new RuntimeException("key size must <= MAX/3")
    }
    if (isLeaf) {
      // 无需分裂
      if (!isLeafSplit(key)) {
        innerInsert(key)
      } else {
        // 需要分裂

      }
    }
  }

  /**
    * 因为borrowKey的话，需要将child也borrow过来
    * @param key
    * @return
    */
  private def getBorrowKeyLength(key: Tuple): Int = {
    if (!isLeaf) {
      Item.getItemLength(key) + ItemConst.INT_LENGTH
    } else {
      Item.getItemLength(key)
    }
  }

  def getEntries: List[Tuple] = entries.toList

  def isLeaf: Boolean = _isLeaf

  def isLeafSplit(key: Tuple): Boolean = {
    if (bPPage.cacluateRemainFreeSpace < Item.getItemLength(key)) {
      return true
    }
    false
  }



}
