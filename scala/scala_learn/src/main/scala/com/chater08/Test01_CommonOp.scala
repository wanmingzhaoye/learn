package com.chater08

object Test01_CommonOp {
  def main(args: Array[String]): Unit = {

    val ls: List[String] = List("a", "b", "c")
    val set: Set[String] = Set("a", "b", "a")
    // 获取集合长度
    val length: Int = ls.length
    // 获取集合大小
    val size: Int = set.size

    for (elem <- ls )
    {
      println(elem)
    }

    ls.foreach(println)
    val iterator: Iterator[String] = ls.iterator
    while (iterator.hasNext)
    {
      println(iterator.next())
    }

    val bool: Boolean = ls.contains("a")
    val bool1: Boolean = set.contains("a")
    ls.sorted(Ordering[String].reverse)
    ls.sortWith(_ < _)
    ls.map((a:String) => a+1 )
    ls.map(a => a+1)
    ls.map(_ + 1)



  }

}
