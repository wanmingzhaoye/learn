package com.chapter07

object Test03_List {
  def main(args: Array[String]): Unit = {
    // (值)不可变list列表 以一定顺序排序的一组数据
    val ls: List[Int] = List(20, 30, 40)
    println(ls)
    println(ls(0))

    ls.foreach(println)

    println("===========")
    val ints: List[Int] = 12 :: ls
    println(ints)
    val ls1: List[Int] = ls.+:(12)
    //List(12, 20, 30, 40)
    println(ls1)
    val ls2: List[Int] = ls1.::(50)
    List(50, 12, 20, 30, 40)
    println(ls2)
    val ls3: List[Int] = Nil.::(50)
    //List(50)
    println(ls3)
    //默认这样创建list
    val ls4 = 32 :: 24 :: 26 :: Nil
    //List(32, 24, 26)
    println(ls4)

    val ls5: List[Any] = ls4 :: ls3
    //List(List(32, 24, 26), 50)
    println(ls5)

    val ls6: List[Int] = ls4 ::: ls3
    //List(32, 24, 26, 50)
    println(ls6)

    val ls7: List[Int] = ls4 ++ ls3
    //List(32, 24, 26, 50)
    println(ls7)
    println("====")
    println(ls1)
    println(ls2)
    println(ls3)


  }

}
