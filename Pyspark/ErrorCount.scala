import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkContext

object ErrorCount extends App {
  Logger.getLogger("org").setLevel(Level.ERROR);
  
  val sc= new SparkContext("local[*]","ErrorCount")
  val data = List("ERROR: Wed Aug 23 10:37:51 BST 2024",
"ERROR: Tue Jul 29 10:37:51 BST 2024",
"WARN: Thu Oct 26 10:47:51 BST 2024",
"WARN: Wed Jan 07 10:37:51 GMT 2024",
"ERROR: Mon Sep 08 10:47:51 BST 2014",
"ERROR: Sun Mar 22 10:37:51 GMT 2024",
"WARN: Sat Jan 13 10:27:51 GMT 2024",
"WARN: Tue Apr 26 10:37:51 BST 2023",
"WARN: Fri Oct 17 10:27:51 BST 2023")

val rdd1 = sc.parallelize(data)
val rdd2 = rdd1.map(x => (x.split(":")(0),1) )
val rdd3 = rdd2.reduceByKey((x,y) => x+y)

rdd3.collect().foreach(println)
   scala.io.StdIn.readLine()
  
}
