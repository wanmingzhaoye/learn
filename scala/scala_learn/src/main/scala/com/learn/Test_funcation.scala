package com.learn

object Test_funcation {

  def main(args: Array[String]): Unit = {

    def Test(name:String):String=
    {
     return name
    }
//    （1）return 可以省略，Scala 会使用函数体的最后一行代码作为返回值

    def Test1(name:String):String=
    {
       name
    }
//    （2）如果函数体只有一行代码，可以省略花括号
    def Test2(name:String):String=name

//    （3）返回值类型如果能够推断出来，那么可以省略（:和返回值类型一起省略）
    def Test3(name:String)=name

//    （4）如果有 return，则不能省略返回值类型，必须指定
      def Test4(name:String):String=
        {
        return name
        }
//    （5）如果函数明确声明 unit，那么即使函数体中使用 return 关键字也不起作用
      def Test5(name:String):Unit=
        {
         return name
        }
    //
//    （6）Scala 如果期望是无返回值类型，可以省略等号
    def Test6(name:String):Unit=print(name)
    def Test7(name:String)=print(name)
    def Test8(name:String)
    {
      print(name)
    }
//    （7）如果函数无参，但是声明了参数列表，那么调用时，小括号，可加可不加
    def Test9()=print("aaa")
    Test9
    Test9()
//    （8）如果函数没有参数列表，那么小括号可以省略，调用时小括号必须省略
    def Test10=print("aaa")
    Test9
//    （9）如果不关心名称，只关心逻辑处理，那么函数名（def）可以省略
val stringToFunction: String => Any => Nothing = (x: String) => {
  return _
}
  }
}

