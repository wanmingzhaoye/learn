package com.chapter07

import scala.collection.immutable.HashMap


object Test08_ImutableMap {
  def main(args: Array[String]): Unit = {
    //不可变map(k,V)
    val map: Map[String, Int] = Map("a" -> 10, "b" -> 5)
    val map1: Map[String, Int] = Map(("a",10), ("b",5))
    println(map)

    map.foreach(println)
    map.foreach((kv:(String,Int)) => println(kv))

    println("==========")
    for (key <- map.keys)
    {
      println(map.get(key))
    }
    //为空返回0
    println(map.getOrElse("a",0))
    println(map.getOrElse("bc",0))
    //抛出异常，代码执行继续
    println(map("bc"))
    val map2: Map[String, Int] = map.+(("c", 10))

  }

}
