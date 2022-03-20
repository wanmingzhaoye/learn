package com.chater08

object Test05_WrodCont {
  def main(args: Array[String]): Unit = {
    val ls: List[String] = List(
      "hadoop hadoop scala",
      "scala hadoop",
      "hello world",
      "hello scala",
      "hrro hello",
      "spark good",
      "spark hello scala"
    )
    ls.flatMap(_.split(" "))
      .map((_,1))
      .groupBy(_._1)
      .mapValues(list =>
      {
        list.size
      })
      .toList
      .sortWith(_._2 >= _._2).take(3)
      .foreach(println)

    val fmlist : List[String] = ls.flatMap(_.split(" "))
    val mplsit: List[Array[String]] = ls.map(_.split(" "))
    val gfmls: Map[String, List[String]] = fmlist.groupBy(i => i)
    val stringToInt: Map[String, Int] = gfmls.map(kv => (kv._1, kv._2.size))

    println(stringToInt)

    val list: List[(String, Int)] = stringToInt.toList
    println(list)

    val tuples: List[(String, Int)] = list.sortWith(_._2 > _._2).take(3)
    println(tuples)




  }

}
