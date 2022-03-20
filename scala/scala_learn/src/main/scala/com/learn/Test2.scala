package com.learn

object Test2 {
  def main(args: Array[String]): Unit = {
    var a =10
    var  a1 =10
    val a2 =1
    //a="aaa"
    //Scala是强类型语言
   // var ar 必须赋值
   val sd= new Student("a",10)
    sd
    //val  sd 不能改 里面的属性是var的能改
    sd.name="b"
    val -+ = 10
    //val `print` =0
    var s = "adada"
    val b ="da"
    s=s+b
    print(s*3)
    //printf 格式化输出
    //%d 十进制数字
    //%s 字符串
    //%c 字符
    //%e 指数浮点数
    //%f 浮点数
    //%i 整数（十进制）
    //%o 八进制
    //%u 无符号十进制
    //%x 十六进制
    //%% 打印%
    //% 打印%
    printf("%s在刷%s挖","aa","cc")

    //s""字符串模版 前面加一个s(字符串) 前面加f”“浮点 ran”“之间值填进来$｛｝外的值不变
    printf(s"${s}在刷${b}")
    //S""" 三引号 多还数据保持原数据，包过空格换行
    val test= s"""
       |select *
       |from
       |  student
       |where
       |name=${a}
       |and
       |age=${b}
       |""".stripMargin

    println(test)


  }
}
