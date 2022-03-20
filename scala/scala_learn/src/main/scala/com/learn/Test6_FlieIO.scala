package com.learn

import java.io.{File, PrintWriter}
import scala.io.Source

//文件的输入输出
object Test6_FlieIO {

  def main(args: Array[String]): Unit = {

    //读取文件
   val source= Source.fromFile("src/main/resources/test.txt")

    source.getLines().foreach(println)



   val writer= new PrintWriter(new File("src/main/resources/out.txt"))
    writer.write("aaa,bbb")
    writer.close()



  }

}
