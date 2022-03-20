package com.learn

object Test_7 {
  def main(args: Array[String]): Unit = {
    var n:Int = 10

    while (n>=1)
    {
      println(n)
      n -= 1
    }

    def Mywhile(con: => Boolean): (=>Unit) => Unit =
    {
      def doloop(op: => Unit):Unit=
      {
        if (con)
        {
          op
          Mywhile(con)(op)
        }
      }
      doloop _
    }

    n=10
    Mywhile(n>=1){
      n -= 1
      println(n)}
     println("=========")
    def Mywhile1(con: => Boolean): (=>Unit) => Unit=
    {
       op=> if (con) {
        op
        Mywhile(con)(op)
      }
    }
    n=10
    Mywhile1(n>=1){
      n -= 1
      println(n)}

    def Mywhile2(con: => Boolean)(op: => Unit): Unit ={
       if (con) {
        op
        Mywhile(con)(op)
      }
    }
 }

}
