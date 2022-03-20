package com.chater09

object Test01_exception {

  def main(args: Array[String]): Unit = {
    try{
      val i: Int = 10 / 0
    }catch{
      case e:ArithmeticException => println("算数异常")
      case e:Exception => println("普通异常")
    }finally {
        println("结束")
    }
  }

}
