package com.leo.xiaoku.meta

import com.leo.xiaoku.meta.factory.RelFactory
import com.leo.xiaoku.store.fs.FStore
import com.leo.xiaoku.store.item.Item
import com.leo.xiaoku.store.page.Page

import scala.collection.mutable

class Relation {

  // relation包含的元组描述
  private var tupleDesc: TupleDesc = _
  // Relation对应的FilePath
  private var relPath: String = _
  // Relation对应的metaPath
  private var metaPath: String = _
  // page的数量，初始化为1，因为0是pageoffset信息
  private var pageCount: Int = 1
  // pageCount是否change，if change，则更新pageOffSet信息
  private var isPageCountDirty: Boolean = _
  // 装载具体数据信息
  private var relStore: FStore = _
  // 装载原信息
  private var metaStore: FStore = _
  // 页号/偏移 映射
  private var pageOffSetMap: mutable.HashMap[Int, Int] = _
  // 页号/PageLoad映射
  private var pageMap: mutable.HashMap[Int, Page] = _

  def insert(item: Item) = {
    val itemLength = item
  }

  def open(): Unit = {
    relStore = new FStore(relPath)
    relStore.open()
    metaStore = new FStore(metaPath)
    metaStore.open()
    // 初始化pageOffSetMap
    pageOffSetMap = new mutable.HashMap[Int, Int]()
    pageMap = new mutable.HashMap[Int, Page]()
  }

  // 读relation的原信息
  def readMeta(): Unit = {
    // 原信息只有一页

  }

  def setRelPath(relPath: String): Relation = {
    this.relPath = relPath
    this
  }

  def setMetaPath(metaPath: String): Relation = {
    this.metaPath = metaPath
    this
  }



}

object Relation {
  val META_PAGE_INDEX = 0
  val PAGE_OFFSET_INDEX = 0
}
