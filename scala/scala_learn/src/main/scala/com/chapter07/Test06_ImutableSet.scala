package com.chapter07

object Test06_ImutableSet {
  def main(args: Array[String]): Unit = {
    //不可变，无序无重复列表
    val set: Set[Any] = Set(10, 12, "56", 11, 10)
    println(set)
    val set1: Set[Any] = set + 2
    println(set1)
    println(set)

    val set2: Set[Any] = set ++ set1
    println(set2)
    set2.foreach(print)

    val set3: Set[Any] = set2 - 12
    println(set3)

    set3.foreach(println)

  }

}
