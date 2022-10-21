
import happybase 
connection=happybase.Connection('192.168.56.101',port=9090,autoconnect=True)
if(connection):
   print("hbase connection with Hbase ")
else:
   print("Not connecting")

connection.open()

print("List of Tables {0}".format(connection.tables()))

#creating table schema

try:
   connection.create_table(
    'usertable',
    {'cf1':dict(),
     'cf2':dict(),
      'cf3':dict()
    }
)
except Exception as e:
   print(e)
else:
   print("table sucessfully created")


#inserting data
table = connection.table('usertable')

try:
   table.put(b'row-key1', {b'cf1:col1': b'Avijit',b'cf1:col2': b'25'})

   table.put(b'row-key2', {b'cf1:col1': b'Anik', b'cf1:col2': b'36'})
   table.put(b'row-key3', {b'cf2:col3': b' fever medical bill', b'cf2:col4': b'3000'})
   table.put(b'row-key4', {b'cf2:col3': b' pox medical bill', b'cf2:col4': b'4400'})
   table.put(b'row-key5', {b'cf3:col5': b' car brand', b'cf2:col6': b'BMW'})

except Exception as e:
   print(e)
else:
   print("sucessfully inserted data")




print("Scanning over rows in a table")
for key, data in table.scan():
    print(key, data)
   




try:
   table.delete(b'row-key5', columns=[b'cf3:col5', b'cf2:col6'])
except Exception as e:
   print(e)
else:
   print(" deleting record sucessfully")

