package com.leo.xiaoku.store

import com.leo.xiaoku.meta.Relation
import com.leo.xiaoku.meta.factory.RelFactory
import org.junit.Test

class RelationTest {

  @Test
  def relationTest(): Unit = {
    val rela = RelFactory.getInstance().newRelation("t_ku")
    rela.open()
    rela.readMeta()
  }

}
