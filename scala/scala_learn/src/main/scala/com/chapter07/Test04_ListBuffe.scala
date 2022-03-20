package com.chapter07

import scala.collection.mutable.ListBuffer

object Test04_ListBuffe {
  def main(args: Array[String]): Unit = {

    //可变列表

    val lsbf: ListBuffer[Int] = ListBuffer(5,6,8)
    val lsbf2: ListBuffer[Int] = new ListBuffer[Int]()

    lsbf2.append(12,12,12,13)
    println(lsbf)
    println("===========")

    lsbf.append(12)
    lsbf.append(12,15)
    lsbf.insert(2,5,13)

    println(lsbf)
    println("============")

    val lsbf1: lsbf.type = lsbf ++= lsbf2
    println(lsbf)
    println(lsbf2)
    println(lsbf1)
    val lsbf3: lsbf2.type = lsbf ++=: lsbf2
    println(lsbf)
    println(lsbf2)
    println(lsbf3)

    lsbf3.update(12,5)
    lsbf3(0)=99
    println(lsbf3)
    lsbf3.remove(0)





  }

}
