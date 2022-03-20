package com.chater08

object Test03_Reduce {
  def main(args: Array[String]): Unit = {
    val ls: List[Int] = List(1, 2, 3, 4, 5)
    println(ls.reduce(_+_)) //(((1+2)+3)+4+5)
    println(ls.reduce(_-_))  //((((1-2)-3)-4)-5)  从前往后
    println(ls.reduceRight(_-_)) // 1-(2-(3-(4-5))) 从后往前
    //op(head, tail.reduceRight(op))
    //(a,b => a-b)
    //(head1-(head2-(head3-(head4-(head5-head6))))
    //op(head,op(head,op(head,op(head,op(head,op(head))))))
    //(1-(2-(3-(4-(5-(6-(7-(8-9))))))))
    println(test(ls,10)(_+_))
    println(ls.fold(10)(_+_))  //(((((10+1)+2)+3)+4)+5)
    println(ls.fold(10)(_-_))  //10-1-2-3-4-5-
    println(ls.foldLeft(10)(_-_)) //10-1-2-3-4-5-
    println(ls.foldRight(10)(_-_)) // (1-(2-(3-(4-(5-(6-(7-(8-(9-(10-10))))))))))
    println((1-(2-(3-(4-(5-(6-(7-(8-(9-(10-10)))))))))))
    // reverse.foldLeft(z)()(right, left => op(left, right)) 修改了函數將成
    //op: (B, A) => B
    //(right, left) => {op(left, right)}
    //z
    //acc = op(acc, these.head)
    //acc = op(these.head, acc) (1-(2-(3-(4-(5-(6-(7-(8-(9-(10-10))))))))))
    //
   def test(b:List[Int],a:Int)(op:(Int,Int) => Int ):Int=
   {
     var result =a
     b.foreach(x=>{result = op(result,x)})
     result
   }

  }

}
