package com.chapter07

import scala.collection.mutable

object Test07_Set {
  def main(args: Array[String]): Unit = {
    //可变set scala.collection.mutable
    val muset: mutable.Set[Int] = mutable.Set(12, 13, 12, 12, 34, 12, 32, 15)

    val muset1: mutable.Set[Int] = muset + 11 + 19
    println(muset)
    println(muset1)
    println("==========")
    muset += 11
    println(muset)
    println("==========")
    muset.add(11)
    println(muset)
    muset.remove(11)

    muset1 ++= muset
    println(muset)
    println(muset1)






  }

}
