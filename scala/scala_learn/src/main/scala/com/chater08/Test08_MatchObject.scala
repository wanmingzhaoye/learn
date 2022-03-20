package com.chater08


object Test08_MatchObject {
  def main(args: Array[String]): Unit = {
    val alence: Student = new Student("alence", 5)
    val alence1: Student = Student("alence", 10)
    alence1 match {
      case Student("alence",5) => println("同一人")
      case _ =>println("不是同一人")
    }

    val tuples: List[(String, Int)] = List(("a", 0), ("b", -5), ("v", 2), ("e", 5), ("s", -5))

    val tuples1: List[(String, Int)] = tuples.map({
      case (key, v) if v > 0 => (key, v * 2)
      case (key, v) if v == 0 => (key, 0)
      case (key, v) if v < 0 => (key, (-v) * 2)
    })
    println(tuples1)
    val stringToInt: Map[String, Int] =Map(("a", 0), ("b", -5), ("v", 2), ("e", 5), ("s", -5))
    val stringToInt1: Map[String, Int] = stringToInt.map(
      {
        case (key, v) if v > 0 => (key, v * 2)
        case (key, v) if v == 0 => (key, 0)
        case (key, v) if v < 0 => (key, (-v) * 2)
      }
    )
    println(stringToInt1)
  }


}

case class Student(val name:String,val age:Int)

//object Student{
//  def apply(name: String, age: Int): Student = new Student(name, age)
//
//  //Option 返回值为None 或者为Some
//  def unapply(student:Student):Option[(String,Int)]={
//    if (student==null) None else Some(student.name, student.age)
//  }
//}
