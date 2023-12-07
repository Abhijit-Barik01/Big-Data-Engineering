package OOPS

object opps3 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
abstract class Animal{
  val x :Int = 4;
  def eat
}

trait oops3 {
  def bark = {println("Hello how r u")}
  def walk
}

class Dog extends  Animal with oops3{
  def walk  = println("hey motherfuvker whatsup")
  def eat = println("hey sweetie")
}
val d = new Dog                                   //> d  : OOPS.opps3.Dog = OOPS.opps3$Dog$1@380fb434
d.walk                                            //> hey motherfuvker whatsup
d.x                                               //> res0: Int = 4

  
}
