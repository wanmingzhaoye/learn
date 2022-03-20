package com.chater08

object Test07_Macth {
  def main(args: Array[String]): Unit = {

    val i =1
    val str: String = i match {
      case 1 => "one"
      case 2 => "tow"
      case _ => "util"
    }
   println(i)

    val a: Int = 10
    val b: Int = 20
    def test(op:Char):Int = op match {
      case '+'  => a+b
      case '-'  => a-b
      case '*'  => a*b
      case '/'  => a/b
      case '_'  => 0
    }

    def test1(op:Int):Int=op match {
      case i if i >=0 => i
      case i if i < 0 => -i
    }
    println(test('/'))

    def  test2(ts:Any) = ts match {
      case i:Int => println("int")
      case i:String => println("String")
      case i:Char => println("Char")
      case i:List[String] => println("List[String]")
      case i:List[Int] => println("List[Int]")
      case i:Array[Int] => println("Array[Int]")
      case i:Array[String] => println("Array[String]")
      case _ => println("null")
    }
    test2(1)
    test2("aaa")
    test2('a')
    //list 有類型擦除 array没有
    test2(List("aaa"))
    test2(List(5))
    test2(Array(1))
    test2(Array("sss"))



    val list: List[Array[_ >: Int]] = List(
      Array(0, 1, 2),
      Array(0),
      Array("0", 1, "hello"),
      Array(1, 2),
      Array("hhh",1,"ggg")
    )
    for (i <- list) i match {
      case Array(0) => println(i.mkString("-")+"Array(0)")
      case Array(0,_*) => println(i.mkString("-")+"Array(0,_*)")
      case Array(x,y) => println(x+"-"+y+"Array(x,y)")
      case Array(x,1,y) => println(x+"-1-"+y+"Array(x,1,y)")
      case _ => println("null")
    }

    val ls: List[Int] = List(1, 2, 3, 4, 5)

    ls match {
      case a :: b :: c => println("\na=" + a + "\nb=" + b + "\tc=" + c)
      case _ => println("不足2位")
    }

    val stringToTuple: Map[String, (Int, Int)] = Map(("a", (2, 1)), (("b", (3, 2))))
    println(stringToTuple)
    stringToTuple.foreach(println)
  }

}
