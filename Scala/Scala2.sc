object scala2 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  
  def square(x:Int): Int={
  
   x*x
  }                                               //> square: (x: Int)Int
  
  def square1(x:Int)=x*x                          //> square1: (x: Int)Int
  
   def cubeIt(x:Int):Int=x*x*x                    //> cubeIt: (x: Int)Int
   
   cubeIt(2)                                      //> res0: Int = 8
   square1(5)                                     //> res1: Int = 25
   
   
   //higher order function. It is taking another function as parameter.
   def transformInt(x:Int,f:Int=>Int):Int = {
   
      f(x)
   }                                              //> transformInt: (x: Int, f: Int => Int)Int
   
   transformInt(2, square1)                       //> res2: Int = 4
   
   //anonymous function
   transformInt(2,x=>x*x*x*x)                     //> res3: Int = 16
   
   def dividedByTwo(x:Int)={
    x/2
   
   }                                              //> dividedByTwo: (x: Int)Int
   
   
   dividedByTwo(4)                                //> res4: Int = 2
   transformInt(4,x=>x/2)                         //> res5: Int = 2
   transformInt(2,x=>{val y=x*2;y*y})             //> res6: Int = 16
   
   
   
}
