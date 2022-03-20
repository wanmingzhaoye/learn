package com.chater09

object Test02_implicit {
  def main(args: Array[String]): Unit = {

  implicit def convert(num:Int): MyRichInt=  MyRichInt(num)


    12.Mymax2(15)
    println(15.Mymin2(156))

    //每种类型的隐式变量只能定义一个 只有一个隐式参数函数调用时不用() 隐式函数参数默认值优先极低于隐式变量
    implicit val name:String = "alicen"
    implicit val age:Int = 12
    def getname(implicit n:String):Unit={println(n) }
    getname
    def getage(implicit a:Int):Unit={println(a) }
    getage
    def getname1():Unit={println(implicitly[String]) }
    getname1
    def getage1():Unit={println(implicitly[Int]) }
    getage1

  }
   implicit class MyRichInt2(val self: Int)
  {
    def Mymax2(i:Int):Int={ if(i>self) i else self}
    def Mymin2(i:Int):Int={ if(i<self) i else self}
  }

}
case class MyRichInt(val self: Int)
{
  def Mymax(i:Int):Int={ if(i>self) i else self}
  def Mymin(i:Int):Int={ if(i<self) i else self}

}
