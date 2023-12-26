from pyspark import SparkContext
from sys import stdin

sc = SparkContext("local[*]","MovieRating")
sc.setLogLevel("ERROR")
rdd1 = sc.textFile("C:/dataset/movie.txt")
rdd2 = rdd1.map(lambda record : record.split("\t")[2])
# [(5,1),(4,1),(2,1),(5,1)]
#result = rdd2.reduceByKey(lambda a,b : a+b).collect()
#[(5,2),(4,1)]

result = rdd2 .countByValue()
for rating,count in result.items():
    print(rating,count)

stdin.readline()
