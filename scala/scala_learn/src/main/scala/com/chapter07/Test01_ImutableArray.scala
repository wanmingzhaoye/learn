package com.chapter07

//不可变数组:增减加会返回新的数组
object Test01_ImutableArray {
  def main(args: Array[String]): Unit = {


    val array: Array[Int] = new Array[Int](5)

    var array1 =Array(12,12,13,15,14)
    var empty: Array[Int] = Array.empty[Int]
    empty=array1
    array1=empty
    val ints2: Array[Int] = empty ++ array1
    array1.foreach(println)
    empty.foreach(println)
    println(array1==empty)
    val ints: Array[Int] = array :+ 10
    ints.update(0,10)
    ints(0)=1
    val ints1: Array[Int] = ints.map(_ + 1)
    array.update(0,1)
    println(array(1))
    array1(0)=5
    array1(2)=2
    for (i <- 0 until array1.length)
    {
      println(array1(i))
    }
    for (i <- array1.indices)
    {
      println(array1(i))
    }
    for(elem <- array1) print(elem+" ")
    val  arr: Iterator[Int] = array1.iterator
    while (arr.hasNext) print(arr.next() + " ")

    array1.foreach(println)

    print(array1.mkString("--"))

    //二维
    val array2 =Array.ofDim[Int](2, 2)
    array2(0)(0) = 1
    array2(1)(0) = 2

    for (i <- 0 until array2.length; j <- 0 until array2(i).length)
    {
      println(array2(i)(j))
    }

    for (i <- array2.indices; j <- array2(i).indices)
    {
      print(array2(i)(j))
      if (j==array2(i).length-1) println
    }

    array2.foreach(i=>i.foreach(println))
    array2.foreach(_.foreach(println))

  }



}
