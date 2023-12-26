from pyspark import SparkContext
from sys import stdin
sc = SparkContext("local[*]","Top-Customer")
sc.setLogLevel("ERROR")
rdd1 = sc.textFile("C:/dataset/customerorder.csv")
rdd2 = rdd1.map(lambda x : (x.split(",")[0],float(x.split(",")[2])))
rdd3 = rdd2.reduceByKey(lambda x,y: x+y)
rdd4 = rdd3.sortBy(lambda x : x[1],False)
result = rdd4.collect()
for a in result:
    print(a)

stdin.readline()
