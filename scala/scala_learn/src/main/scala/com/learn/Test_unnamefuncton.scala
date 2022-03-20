package com.learn

object Test_unnamefuncton {

  def main(args: Array[String]): Unit = {

    val stringToUnit: String => Unit = (name: String) => {println(name)}
    val f =stringToUnit
    def fun(nfun:String=>Unit ):Unit =
    {
      nfun("test")
    }
    def fun2(nfun:(String,String)=>String ):String =
    {
      nfun("test","testb")
    }
    fun(stringToUnit)
    fun((name:String)=>{println(name)})
//    （1）参数的类型可以省略，会根据形参进行自动的推导
    fun((name)=>{println(name)})

//    （2）类型省略之后，发现只有一个参数，则圆括号可以省略；其他情况：没有参数和参
    fun(name=>{println(name)})
//    参数超过 1 的永远不能省略圆括号。
    def fun1(nfun:(String,String)=>Unit): Unit =
    {
      nfun("test1","test2")
    }
    fun1((a,b)=>{println(a+b)})
//    （3）匿名函数如果只有一行，则大括号也可以省略
    fun1((a,b)=>println(a+b))
//    （4）如果参数只出现一次，则参数省略且后面参数可以用_代替
    fun1((a,b)=>println(a+b))
    println(fun2((a,b)=>a+b))
    println(fun2(_+_))
  }

}
