package com.leo.xiaoku.meta.factory

import com.leo.xiaoku.config.SystemConfig
import com.leo.xiaoku.meta.Relation

class RelFactory {

  def newRelation(tableName: String): Relation = {
    val relation = new Relation
    relation.setRelPath(SystemConfig.RELATION_FILE_PRE_FIX + tableName)
    relation.setMetaPath(SystemConfig.FREEDOM_REL_META_PATH + tableName)
    relation
  }

}

object RelFactory {

  private var relFactory: RelFactory = new RelFactory

  def getInstance(): RelFactory = relFactory

}
