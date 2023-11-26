object scala1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  
  val a:String ="hello"                           //> a  : String = hello
  println(a)                                      //> hello
  var b:String="hi"                               //> b  : String = hi
  b="there";
  println(b)                                      //> there
  val numberone: Int=5;                           //> numberone  : Int = 5
   val d=true //this is autometically type infer  //> d  : Boolean = true
   
   val pi :Double =3.146;                         //> pi  : Double = 3.146
    
    val z :Float=8.15f;                           //> z  : Float = 8.15
   val e: Long =123455;                           //> e  : Long = 123455
   
   //s interpolaration
   var name:String="Sumit"                        //> name  : String = Sumit
   println(s"hello $name how are you")            //> hello Sumit how are you
   
   //p interpolaration
   
   println(f"value of z is $z%.1f")               //> value of z is 8.1
   //raw interpolaration
   println(raw"hello how  \n are you")            //> hello how  \n are you
   
   
   //string compaarasion
   val  x: String="sumit"                         //> x  : String = sumit
   val y:String="sumit"                           //> y  : String = sumit
   
   val result :Boolean=x==y;                      //> result  : Boolean = true
   
   
   //if -else
   if(1>3){
   println("hello");
   }else{
   println("there");
   
   }                                              //> there
   
   //MATCH is like  switch-CASE
   
   val num =1                                     //> num  : Int = 1
   num match{
   
   case 1=> println("one")
   case 2=>println("two")
   case 3=>println("three")
   case _=>println("something else")
   }                                              //> one
   
   
   for(x<- 1 to 10)
   {
    val squared=x*x
    println(squared)
   }                                              //> 1
                                                  //| 4
                                                  //| 9
                                                  //| 16
                                                  //| 25
                                                  //| 36
                                                  //| 49
                                                  //| 64
                                                  //| 81
                                                  //| 100
   var i=0                                        //> i  : Int = 0
   while(i<=10){
   
   println(i)
   i=i+1;
   
   
   }                                              //> 0
                                                  //| 1
                                                  //| 2
                                                  //| 3
                                                  //| 4
                                                  //| 5
                                                  //| 6
                                                  //| 7
                                                  //| 8
                                                  //| 9
                                                  //| 10
 //do-while
    var  xx=0                                     //> xx  : Int = 0
   do{
   
   println(xx)
   xx = xx +1
   }while(xx <=10)                                //> 0
                                                  //| 1
                                                  //| 2
                                                  //| 3
                                                  //| 4
                                                  //| 5
                                                  //| 6
                                                  //| 7
                                                  //| 8
                                                  //| 9
                                                  //| 10
 //expression block last will return
 
 {
 var m=9;
 m=m+1
 7
 }                                                //> res0: Int = 7
   
   
}
