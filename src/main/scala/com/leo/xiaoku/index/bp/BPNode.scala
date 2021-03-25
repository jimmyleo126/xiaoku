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
  protected var entries: ListBuffer[Tuple] =  ListBuffer[Tuple]()
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
        // 则分裂成左右两个节点
        val left = new BPNode(true, bPTree)
        val right = new BPNode(true, bPTree)
        if (previous != null) {
          previous.setNext(left)
          left.setPrevious(previous)
        }
        if (next != null) {
          next.setPrevious(right)
          right.setNext(next)
        }
        if (previous == null) {
          tree.setHead(left)
        }
        left.setNext(right)
        right.setPrevious(left)
        previous = null
        next = null
        // 插入后再分裂
        innerInsert(key)
        val leftSize = this.entries.size / 2
        val rightSize = this.entries.size - leftSize

        // 左右节点，分别赋值
        for (i <- 0 until leftSize) {
          left.getEntries += entries(i)
        }
        // 叶子节点需要全拷贝
        for (i <- 0 until rightSize) {
          right.getEntries += entries(leftSize + i)
        }

        // 表明当前节点不是根节点
        if (parent != null) {
          // 调整父子节点的关系
          // 寻找到当前节点对应的index
          val index = parent.getChildren.indexOf(this)
          // 删掉当前节点
          parent.getChildren.remove(index)
          left.setParent(parent)
          right.setParent(parent)
          // 将节点增加到parent上面
          parent.getChildren.insert(index, left)
          parent.getChildren.insert(index + 1, right)
          // 回收
          recycle()
          // 插入关键字
          parent.innerInsert(right.getEntries.head)
          // 更新
          parent.up


        }

      }
    }
  }

  protected def updateInsert(tree: BPTree): Unit = {
    // 当前页面存不下，需要分裂
    if (isNodeSplit) {
      val left = new BPNode(false, bPTree)
      val right = new BPNode(false, bPTree)
      val leftSize = this.entries.size / 2
      val rightSize = this.entries.size - leftSize
      // 左边复制entry
      for
    }
  }

  def isNodeSplit: Boolean = {
    if (bPPage.cacluateRemainFreeSpace > 0) {
      return true
    }
    false
  }

  private def recycle(): Unit = {
    setEntries(null)
    setChildren(null)
    bPTree.recyclePageNo(pageNo)
  }

  def setEntries(entries: ListBuffer[Tuple]): BPNode = {
    this.entries = entries
    this
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

  def getEntries: ListBuffer[Tuple] = entries

  def isLeaf: Boolean = _isLeaf

  def isLeafSplit(key: Tuple): Boolean = {
    if (bPPage.cacluateRemainFreeSpace < Item.getItemLength(key)) {
      return true
    }
    false
  }

  def getNext: BPNode = next

  def setNext(next: BPNode): BPNode = {
    this.next = next
    this
  }

  def getPrevious: BPNode = previous

  def setPrevious(previous: BPNode): BPNode = {
    this.previous = previous
    this
  }

  def getChildren: ListBuffer[BPNode] = children

  def setChildren(children: ListBuffer[BPNode]): BPNode = {
    this.children = children
    this
  }

  def getParent: BPNode = parent

  def setParent(parent: BPNode): BPNode = {
    this.parent = parent
    this
  }



}
