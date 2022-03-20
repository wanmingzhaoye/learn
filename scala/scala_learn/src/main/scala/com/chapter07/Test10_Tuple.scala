package com.chapter07

import scala.collection.Iterator.empty.hasNext

object Test10_Tuple {
  def main(args: Array[String]): Unit = {

    val tp: (String, Int, Char,Boolean) = ("hello", 10, 'a',true)
    println(tp)
    println(tp._1)
    println(tp._2)
    println(tp._3)
    println(tp.productElement(0))

    for (elem <- tp.productIterator)
    {
      println(elem)
    }
    val iterator: Iterator[Any] = tp.productIterator
    while (iterator.hasNext){
      println(iterator.next())
    }
    val tp1: (String, Int, Double, (Int, String)) = ("a", 1, 0.1, (2, "b"))
    println(tp1._4._2)
    println(tp1)
  }

}
