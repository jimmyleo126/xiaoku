package com.leo.xiaoku.index

import com.leo.xiaoku.config.SystemConfig
import com.leo.xiaoku.meta.{Attribute, Relation}
import com.leo.xiaoku.store.fs.FStore

abstract class BaseIndex(relation: Relation, indexName: String, attributes: Array[Attribute]) extends Index{

  // 隶属哪个relation
//  protected var relation: Relation = _
  // 索引名称
//  protected var indexName: String = _
  // 索引用到的属性项
//  protected val attributes: Array[Attribute] = _
  // 索引所在的文件具体位置
  protected val path: String = SystemConfig.RELATION_FILE_PRE_FIX + indexName

  protected val fStore = new FStore(path)
  fStore.open()

//  protected val pageNoAllocator: =
}
