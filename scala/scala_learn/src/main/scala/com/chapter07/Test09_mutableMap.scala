package com.chapter07

import scala.collection.mutable

object Test09_mutableMap {
  def main(args: Array[String]): Unit = {
    val map: mutable.Map[String, Int] = mutable.Map("a" -> 1, "b" -> 3)
    val map1: mutable.Map[String, Int] = mutable.Map("a" -> 3, "b" -> 2,"c"->3)

    map ++= map1

    //添加或更新元素
    map("def") = 456
    map += ("java" -> 20, "scala" -> 30)
    //相同的值修改
    val map2: mutable.Map[String, Int] =  map ++ map1
    println(map)
    println(map1)
    println(map2)
  }
}
