package com.chater08

import scala.collection.immutable
import scala.collection.parallel.immutable.ParSeq

object Test06_Parallel {
  def main(args: Array[String]): Unit = {

    val strings: immutable.IndexedSeq[String] = (1 to 100).map(
      x => {
        Thread.currentThread().getName
      }
    )
    println(strings)

    val strings2: ParSeq[Long] = (1 to 100).par.map(
      x => {
        Thread.currentThread().getId
      }
    )
    println(strings2)
  }

}



