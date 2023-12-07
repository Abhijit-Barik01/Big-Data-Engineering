package OOPS

object oops1 {
  
  class Person
  val p=new Person                                //> p  : OOPS.oops1.Person = OOPS.oops1$Person@5ce65a89
  
  println(p)                                      //> OOPS.oops1$Person@5ce65a89
  
  
  
  
  class Person1(name:String,age:Int)
  {
  
  val x= 20
  def ageDoubler =age*20
  
  def salaryDoubler(salary:Int)=salary*20
  
  
  }
  
  val p1=new Person1("sumit",30)                  //> p1  : OOPS.oops1.Person1 = OOPS.oops1$Person1$1@2f7c7260
  p1.x                                            //> res0: Int = 20
  p1.ageDoubler                                   //> res1: Int = 600
  p1.salaryDoubler(20)                            //> res2: Int = 400
  
  //p1.name
  
}
