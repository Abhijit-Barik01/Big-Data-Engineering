from pyspark import SparkContext
from sys import stdin
sc = SparkContext("local[*]","CountError")
sc.setLogLevel("INFO")
data = ["ERROR: Wed Aug 23 10:37:51 BST 2024",
"ERROR: Tue Jul 29 10:37:51 BST 2024",
"WARN: Thu Oct 26 10:47:51 BST 2024",
"WARN: Wed Jan 07 10:37:51 GMT 2024",
"ERROR: Mon Sep 08 10:47:51 BST 2014",
"ERROR: Sun Mar 22 10:37:51 GMT 2024",
"WARN: Sat Jan 13 10:27:51 GMT 2024",
"WARN: Tue Apr 26 10:37:51 BST 2023",
"WARN: Fri Oct 17 10:27:51 BST 2023"]

rdd1 = sc.parallelize(data)
rdd2 = rdd1.map(lambda x :  (x.split(":")[0],1) )
rdd3 = rdd2.reduceByKey(lambda x,y:x+y)

for x in rdd3.collect():
    print(x)
stdin.readline()






