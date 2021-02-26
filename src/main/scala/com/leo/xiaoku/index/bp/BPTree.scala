package com.leo.xiaoku.index.bp

import com.leo.xiaoku.index.BaseIndex
import com.leo.xiaoku.meta.{Attribute, Relation, Tuple}

class BPTree(relation: Relation, indexName: String, attributes: Array[Attribute]) extends BaseIndex(relation: Relation, indexName: String, attributes: Array[Attribute]){

  override def get(key: Tuple): Unit = ???

  override def remove(key: Tuple): Unit = ???

  override def insert(key: Tuple): Unit = ???
}
