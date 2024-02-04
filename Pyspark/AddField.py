#ADD extra column in existing dataset
def ParseLine(line):
    fields = line.split(",")
    if int(fields[1]) > 18:
        return (fields[0],fields[1],fields[2],"Y")
    else:
        return (fields[0], fields[1], fields[2], "N")

from pyspark import  SparkContext
sc = SparkContext("local[*]","ADDField")
Rdd1 = sc.textFile("C:/dataset/hcl.csv")
Rdd2 = Rdd1.map(ParseLine)
print(Rdd2.collect())
