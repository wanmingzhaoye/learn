package com.learn

object Test_baseType {
  def main(args: Array[String]): Unit = {

    val a:Byte  = -128
    val b:Byte  = 127

    val c =  2//整数默认为int
    //val  d =  12312111111
    val d =  12312111111L

    val f:Byte = (10+20)//可以运行
    //val h:Byte=(b+20) 不可运行 b值要计算才知道
    val h:Byte=(b.toByte+20).toByte
    val i:Float = 0.1454654f
    val char='a'
    var aa=5;
    val bb:Byte =aa.toByte
    val cc:Int=bb
    var dd:Double = aa
    aa=dd.toInt

    for(i <- 1 to 10) //1到10
    {
      println(i)
    }
    for(i <- 1 until  10) //1直到10 不包过10
    {
      println(i)
    }

    for (i <- 1 to 10 if i !=2)
    {
      println(i)
    }

    for (i <- 1 to 10 by 2)
    {
      println(i)
    }

    for (i <- 30 to 10 by 2)
    {
      println(i)
    }
    for (i <- 0.1 to 10.0 by 0.2)
    {
      println(i)
    }
    println("============")

    /*for (i <- 1 to 10; j <- 1 to 3)
    {
      println(i+"a"+j)
    }*/

    for (i <- 1 to 9 )
    {
      for (j <- 1 to i)
      {
        print(i*j+"\t")
      }
      println()

    }

    for (i <- 1 to 9;j <- 1 to i )
    {

      print(i*j+"\t")
      if(i==j)
      {
        println()
      }


    }
  }

}
