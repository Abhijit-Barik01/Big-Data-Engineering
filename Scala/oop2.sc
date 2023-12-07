package OOPS

object oops2 {
                                                  
                                           
               
     object Person{
           // class level functionality
                    val N_EYES=2
                    def canFly:Boolean =false
                                                  
                                                 
     }
                                                  
                                                  
  
  class Person( name:String,age:Int)
  {
   //instance level functionality
  def salaryDouble(salary:Int):Int=salary*2
  
  }
  
  // companion . singletone design
  
  val person=new Person("sumit",70)               //> person  : OOPS.oops2.Person = OOPS.oops2$Person@18769467
  val person1=new Person("sumit",70)              //> person1  : OOPS.oops2.Person = OOPS.oops2$Person@668bc3d5
   println(person1==person)                       //> false
   
  println(person.salaryDouble(20))                //> 40
  println(Person.canFly)                          //> false
  val marry = Person                              //> marry  : OOPS.oops2.Person.type = OOPS.oops2$Person$@3cda1055
  val kerry = Person                              //> kerry  : OOPS.oops2.Person.type = OOPS.oops2$Person$@3cda1055
  println(marry == kerry)                         //> true
}
