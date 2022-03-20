package com.chater08

import scala.collection.mutable

object test04_mergeMap {
  def main(args: Array[String]): Unit = {

    val map1: Map[String, Int] = Map(("a", 1), ("d", 2), ("c", 3), ("b", 4))
    val map2: mutable.Map[String, Int] = mutable.Map(("d", 1), ("e", 2), ("a", 3), ("b", 4))

    //println(map2("c"))
    //map(key)= ... 不存在就添加，存在就更改
    val map3: mutable.Map[String, Int] = map1.foldLeft(map2)(
      (merge, kv) => {
        val key: String = kv._1
        val value: Int = kv._2
        //getOrElse 有返回get(key),沒有返回默認值0
        merge(key) = merge.getOrElse(key, 0) + value
        merge
      }
    )
    println(map1)
    println(map2)
    println(map3)

  }

}
