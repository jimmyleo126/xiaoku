package com.leo.xiaoku.index

import com.leo.xiaoku.meta.Tuple

trait Index {

  // 查询
  def get(key: Tuple)

  // 移除
  def remove(key: Tuple)

  // 插入
  def insert(key: Tuple)
}
