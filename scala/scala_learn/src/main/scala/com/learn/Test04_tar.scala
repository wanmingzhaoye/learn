package com.learn

import scala.collection.immutable

object Test04_tar {

  def main(args: Array[String]): Unit = {

    for (i <- 1 to 9)
    {
      val satr = 2*i -1
      val spaeces = 9 - i
      println(" " * spaeces + "*" * satr)
    }
    val unit = for (i <- 1 to 9; satr = 2 * i - 1; spaeces = 9 - i) {
      println(" " * spaeces + "*" * satr)
    }

    val ints: immutable.IndexedSeq[Int] = for (i <- 1 to 9) yield i*2
    println(ints)
  }

}
