package com.chater08

object Test02_HighLeveFuncation_Map {
  def main(args: Array[String]): Unit = {

    val ls: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    ls.filter(i => i%2==0)

    println(ls.filter(_%2 ==0))

    println("============")
    //map
    println(ls.map(_*2))
    println(ls.map(x => x*x))

    //扁平化
    val ls1: List[List[Int]] = List(List(0, 1, 2), List(2, 4, 6), List(5, 8, 9))

    val ls11: List[Int] = ls1(0) ::: ls1(1) ::: ls1(2)
    
    val ls12: List[Int] = ls1.flatten

    println(ls)
    println(ls11)
    println(ls12)
    println("==================")
    //扁平映射
    val ls2: List[String] = List("hello word", "hello scala", "hello java")

    val ls3: List[String] = ls2.flatMap(_.split(" "))
    println(ls2)
    println(ls3)

    println("==================")

    //按返回結果分組
    val ls4: Map[Int, List[Int]] = ls.groupBy(_ % 2)
    val ls5: Map[String, List[Int]] = ls.groupBy(elem => if (elem % 2 == 0)  "偶數" else "奇數")
    println(ls4)
    println(ls5)

    val ls6: Map[String, List[String]] = ls3.groupBy(_.substring(0,1))

    val ls7: List[String] = List("hello", "word", "hello", "scala", "hello", "java")
    val ls8: Map[String, List[String]] = ls7.groupBy(_.substring(0,1))
    println(ls6)
    println(ls8)







  }

}
