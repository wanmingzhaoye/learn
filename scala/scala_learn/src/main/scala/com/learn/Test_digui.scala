package com.learn

import scala.annotation.tailrec

object Test_digui {

  def main(args: Array[String]): Unit = {

        def test(i: Int):Int =
        {
          if (i==0) return 1
           test(i-1)*i
        }

        def test1(i: Int):Int=
        {
          @tailrec
          def po(j: Int, result:Int):Int=
          {
            if (j==0) return result
            po(j-1,result*j)
          }
          po(i,1)
        }
        println(test1(5))

  }

}
