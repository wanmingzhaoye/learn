package com.learn

// Student(var name: String, var age: Integer) var可以改
class Student(var name: String, var age: Integer) {

  def printinfo(): Unit = {
    println(name, age, Student.school)
  }

}

//引入伴生对象实现静态成员 main 只能在static里才能被识别
object Student {
  val school: String = "xxxxx";

  def main(args: Array[String]): Unit = {

    val student = new Student("a", 1)
    student.printinfo()
  }
}
