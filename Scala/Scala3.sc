object scala3 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  //how to print array contains --two ways
  val a=Array(1,2,3,4,5)                          //> a  : Array[Int] = Array(1, 2, 3, 4, 5)
  println(a.mkString(","))                        //> 1,2,3,4,5
  println(a) // a is pointing array object        //> [I@34b7bfc0
  for(i<-a){
  println(i)                                      //> 1
                                                  //| 2
                                                  //| 3
                                                  //| 4
                                                  //| 5
  }
  
  
    //array is mutable means .It is changable
      a(2)=7
      
      
   //List
      
     val b= List(1,2,3,4);                        //> b  : List[Int] = List(1, 2, 3, 4)
     println(b.head);                             //> 1
     println(b.tail);                             //> List(2, 3, 4)
     println(b(0))                                //> 1
     for(i<-b)println(i)                          //> 1
                                                  //| 2
                                                  //| 3
                                                  //| 4
      b.reverse                                   //> res0: List[Int] = List(4, 3, 2, 1)
      b.sum                                       //> res1: Int = 10
      10::b    //adding element infront of list   //> res2: List[Int] = List(10, 1, 2, 3, 4)
      val c=List("abc",3)                         //> c  : List[Any] = List(abc, 3)
      for(i<-c)println(i)                         //> abc
                                                  //| 3
     //Tuple
     
     
     val tp=("sumit",100,true)                    //> tp  : (String, Int, Boolean) = (sumit,100,true)
     println(tp._1)                               //> sumit
     
     val z=("anik","oop")                         //> z  : (String, String) = (anik,oop)
     val p="anik"->"789"                          //> p  : (String, String) = (anik,789)
     
     
     //range
     
     val rng=1 until 10                           //> rng  : scala.collection.immutable.Range = Range 1 until 10
     var d= 1 to 10                               //> d  : scala.collection.immutable.Range.Inclusive = Range 1 to 10
     
     for(i <- rng)println(i)                      //> 1
                                                  //| 2
                                                  //| 3
                                                  //| 4
                                                  //| 5
                                                  //| 6
                                                  //| 7
                                                  //| 8
                                                  //| 9
    var  s=Set(5,7,8,9)                           //> s  : scala.collection.immutable.Set[Int] = Set(5, 7, 8, 9)
    println(s)                                    //> Set(5, 7, 8, 9)
    s.max                                         //> res3: Int = 9
    s.min                                         //> res4: Int = 5
}
