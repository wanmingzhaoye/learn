package com.learn

object Test_funcationarry {
  def main(args: Array[String]): Unit = {

    val value = Array(1, 5, 9, 10)
    def ArratTow2(array: Array[Int],op: Int => Int) =
      for (elem <- array) yield op(elem)


    def addOp(i:Int)=i+1

    println(ArratTow2(value,addOp).mkString(","))

    println(ArratTow2(value,_*2).mkString(","))

    var fun =( i : Int, j : String, f : Char)
    => if (i==0 && j=="" && f==0) false else true

    println(fun(0,"0",0))
    println("================")

    def f1(i:Int): String =>Char => Boolean =
    {
      def f2(j:String): Char => Boolean =
      {
        def f3(f:Char): Boolean =
        {
           if (i==0 && j=="" && f=='0') false else  true
        }
         f3
      }
       f2
    }
//    def f1(i:Int)(j:String)(f:Char):Boolean =
//    {
//     if (i==0 && j=="" && f=='0') false else  true
//
//    }
    println( f1(0)("")('0'))
    println( f1(2)("dad")('0'))

    def f4(i:Int): String => Char => Boolean =
    {
      j => f => if (i==0 && j=="" && f=='0') false else  true
    }
    println( f4(0)("")('0'))
    println( f4(2)("dad")('0'))

    // 科利化
    def f5(i:Int)(j:String)(f:Char): Boolean = if (i==0 && j=="" && f=='0') false else  true

    println( f5(0)("")('0'))
    println( f5(2)("dad")('0'))
  }


}
