package com.chater08

object Test05_WrodCont1 {
  def main(args: Array[String]): Unit = {
    val ls: List[(String, Int)] = List(
      ("hadoop hadoop scala",2),
      ("scala hadoop",3),
      ("hello world",2),
      ("hello scala",1),
      ("hrro hello",2),
      ("spark good",1),
      ("spark hello scala",3)
    )
    ls.flatMap(
      tuple =>
      {
        tuple._1.split(" ").map(word => (word,tuple._2))
      }
    ).groupBy(word => word._1)
      .mapValues(list => list.map(_._2).sum)
      .toList.sortWith(_._2 >=_._2).take(3)
      .foreach(println)

    val mp: List[String] = ls.map(
      kv => {
        ((kv._1 + " ") * kv._2)
      }
    )
    println(mp)
    mp.flatMap(_.split(" "))
      .groupBy(word => word)
      .map(kv =>(kv._1,kv._2.size))
      .toList.sortWith(_._2>=_._2)
      .take(3)
      .foreach(println)

    val tuples: List[(String, Int)] = ls.flatMap(
      tuple => {
        val strings: Array[String] = tuple._1.split(" ")
        strings.map(strings => (strings, tuple._2))
      }
    )
    println(tuples)
    val grupmap: Map[String, List[(String, Int)]] = tuples.groupBy(word => word._1)
    println(grupmap)

    val stringToInt: Map[String, Int] = grupmap.mapValues(
      tuplelist => {
        tuplelist.map(_._2).sum
      }
    )
    println(stringToInt)
    stringToInt.toList.sortWith(_._2 >= _._2).take(3).foreach(println)

  }

}
