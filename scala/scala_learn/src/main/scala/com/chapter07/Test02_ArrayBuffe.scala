package com.chapter07

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

//可变数组 增减不会返回新的数组
object Test02_ArrayBuffe {
  def main(args: Array[String]): Unit = {

    val buff: ArrayBuffer[Int] = new ArrayBuffer[Int]()
    val ints: ArrayBuffer[Int] = ArrayBuffer(12, 10, 15, 13, 14)
    print(buff.mkString("--"))
    println(ints.mkString("--"))
    println(ints)

    buff.append(1,2,3,45,5)
    buff.prepend(5)
    buff.insert(3,54)
    buff.remove(0)
    buff.insertAll(1,ints)
    buff.prependAll(ints)
    buff.remove(0,5)
    //删除为值12的数
    val buff2: ArrayBuffer[Int] = buff ++ buff
    buff -= 12
    println(buff)
    //可变转不可变
    val array: Array[Int] = buff.toArray
    println(array)
    //不可变转可变
    val buffer: mutable.Buffer[Int] = array.toBuffer
    println(buffer)


  }

}
