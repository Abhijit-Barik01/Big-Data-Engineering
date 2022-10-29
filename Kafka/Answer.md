### Download the dataset from the below mentioned link
https://github.com/shashank-mishra219/Confluent-Kafka-Setup/blob/main/restaurant_orders.csv


### 1. Setup Confluent Kafka Account<br>
 The Confluent Kafka Account was created with the user mail ID and the account was logged in.

### 2. Create one kafka topic named as "restaurent-take-away-data" with 3 partitions<br>
Topic named "restaurent-take-away-data" was created in the kafka cluster named as "demo-kafka-cluster" with 3 partitions and the API Key of the cluster was downloaded.
  
### 3. Setup key (string) & value (json) schema in the confluent schema registry<br>
The schema of the above mentioned dataset was set as the key was just allocated as "string", whereas the value is needed to be passed in json format as shown below :
```
import argparse

from confluent_kafka import Consumer
from confluent_kafka.serialization import SerializationContext, MessageField
from confluent_kafka.schema_registry.json_schema import JSONDeserializer
from confluent_kafka.schema_registry import SchemaRegistryClient



from confluent_kafka.schema_registry import SchemaRegistryClient
import datetime


API_KEY = '5EDWNFRIVKUNMNRV'
ENDPOINT_SCHEMA_URL  = 'https://psrc-vrpp5.us-east-2.aws.confluent.cloud'
API_SECRET_KEY = 'Fn7EquWlsmKgWmXkH6Jkpnz+3YDq4BHyktXyyrp1OYyeIvVEJmAYWFotXqnYKQ4m'
BOOTSTRAP_SERVER = 'pkc-6ojv2.us-west4.gcp.confluent.cloud:9092'
SECURITY_PROTOCOL = 'SASL_SSL'
SSL_MACHENISM = 'PLAIN'
SCHEMA_REGISTRY_API_KEY = 'JECJFMV6IMWVNMNL'
SCHEMA_REGISTRY_API_SECRET = 'xieywUy2MILvFQkq31gmRmkKnNsu4Xpd+X2WNzCzRZFpx7VxvVQTQQPOwHgyuqWV'


def sasl_conf():

    sasl_conf = {'sasl.mechanism': SSL_MACHENISM,
                 # Set to SASL_SSL to enable TLS support.
                #  'security.protocol': 'SASL_PLAINTEXT'}
                'bootstrap.servers':BOOTSTRAP_SERVER,
                'security.protocol': SECURITY_PROTOCOL,
                'sasl.username': API_KEY,
                'sasl.password': API_SECRET_KEY
                }
    return sasl_conf



def schema_config():
    return {'url':ENDPOINT_SCHEMA_URL,
    
    'basic.auth.user.info':f"{SCHEMA_REGISTRY_API_KEY}:{SCHEMA_REGISTRY_API_SECRET}"

    }


class Car:   
    def __init__(self,record:dict):
        for k,v in record.items():
            setattr(self,k,v)
        
        self.record=record
   
    @staticmethod
    def dict_to_car(data:dict,ctx):
        return Car(record=data)

    def __str__(self):
        return f"{self.record}"








def main(topic):



    schema_registry_conf = schema_config()
    schema_registry_client = SchemaRegistryClient(schema_registry_conf) #schema registry client object is created.



    schema_x=schema_registry_client.get_latest_version('restaurent-take-away-data-value').schema.schema_str

    json_deserializer = JSONDeserializer(schema_x,from_dict=Car.dict_to_car)

    consumer_conf = sasl_conf()
    consumer_conf.update({
                     'group.id': 'group1',
                     'auto.offset.reset': "earliest"})

    consumer = Consumer(consumer_conf)
    consumer.subscribe([topic])
    counter=0

    while True:
        try:
            # SIGINT can't be handled when polling, limit timeout to 1 second.
            msg = consumer.poll(1.0)
            if msg is None:
                continue

            car = json_deserializer(msg.value(), SerializationContext(msg.topic(), MessageField.VALUE))

            if car is not None:
                counter+=1
                print(datetime.datetime.now())
                print("User record {}: order: {}\n"
                          .format(msg.key(), car))

                  
                print('Total messages fetched till now:', counter)
        except KeyboardInterrupt:
            break

    consumer.close()

main("restaurent-take-away-data")

```
### 5. From producer code, publish data in Kafka Topic one by one and use dynamic key while publishing the records into the Kafka Topic<br>
 
  ```
  #!/usr/bin/env python
# -*- coding: utf-8 -*-
#
# Copyright 2020 Confluent Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


# A simple example demonstrating use of JSONSerializer.

import argparse
from uuid import uuid4
from six.moves import input
from confluent_kafka import Producer
from confluent_kafka.serialization import StringSerializer, SerializationContext, MessageField
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.json_schema import JSONSerializer
#from confluent_kafka.schema_registry import *
import pandas as pd
from typing import List

FILE_PATH = "C:\\Users\\abarik\\Documents\\bigdata\\kafka\\restaurant.csv"
columns=['order_number','order_date','item_name','quantity','product_price','total_products']

API_KEY = '5EDWNFRIVKUNMNRV'
ENDPOINT_SCHEMA_URL  = 'https://psrc-vrpp5.us-east-2.aws.confluent.cloud'
API_SECRET_KEY = 'Fn7EquWlsmKgWmXkH6Jkpnz+3YDq4BHyktXyyrp1OYyeIvVEJmAYWFotXqnYKQ4m'
BOOTSTRAP_SERVER = 'pkc-6ojv2.us-west4.gcp.confluent.cloud:9092'
SECURITY_PROTOCOL = 'SASL_SSL'
SSL_MACHENISM = 'PLAIN'
SCHEMA_REGISTRY_API_KEY = 'JECJFMV6IMWVNMNL'
SCHEMA_REGISTRY_API_SECRET = 'xieywUy2MILvFQkq31gmRmkKnNsu4Xpd+X2WNzCzRZFpx7VxvVQTQQPOwHgyuqWV'


def sasl_conf():

    sasl_conf = {'sasl.mechanism': SSL_MACHENISM,
                 # Set to SASL_SSL to enable TLS support.
                #  'security.protocol': 'SASL_PLAINTEXT'}
                'bootstrap.servers':BOOTSTRAP_SERVER,
                'security.protocol': SECURITY_PROTOCOL,
                'sasl.username': API_KEY,
                'sasl.password': API_SECRET_KEY
                }
    return sasl_conf



def schema_config():
    return {'url':ENDPOINT_SCHEMA_URL,
    
    'basic.auth.user.info':f"{SCHEMA_REGISTRY_API_KEY}:{SCHEMA_REGISTRY_API_SECRET}"

    }


class Car:   
    def __init__(self,record:dict):
        for k,v in record.items():
            setattr(self,k,v)
        
        self.record=record
   
    @staticmethod
    def dict_to_car(data:dict,ctx):
        return Car(record=data)

    def __str__(self):
        return f"{self.record}"


def get_car_instance(file_path):
    df=pd.read_csv(file_path)
    df=df.iloc[:,:]
    cars:List[Car]=[]
    for data in df.values:
        car=Car(dict(zip(columns,data)))
        cars.append(car)
        yield car

def car_to_dict(car:Car, ctx):
    """
    Returns a dict representation of a User instance for serialization.
    Args:
        user (User): User instance.
        ctx (SerializationContext): Metadata pertaining to the serialization
            operation.
    Returns:
        dict: Dict populated with user attributes to be serialized.
    """

    # User._address must not be serialized; omit from dict
    return car.record


def delivery_report(err, msg):
    """
    Reports the success or failure of a message delivery.
    Args:
        err (KafkaError): The error that occurred on None on success.
        msg (Message): The message that was produced or failed.
    """

    if err is not None:
        print("Delivery failed for User record {}: {}".format(msg.key(), err))
        return
    print('User record {} successfully produced to {} [{}] at offset {}'.format(
        msg.key(), msg.topic(), msg.partition(), msg.offset()))


def main(topic):

    schema= """
    {
  "$id": "http://example.com/myURI.schema.json",
  "$schema": "http://json-schema.org/draft-07/schema#",
  "additionalProperties": false,
  "description": "Sample schema to help you get started.",
  "properties": {
    "order_number": {
      "description": "The type(v) type is used.",
      "type": "number"
    },
    "order_date": {
      "description": "The type(v) type is used.",
      "type": "string"
    },
    "item_name": {
      "description": "The type(v) type is used.",
      "type": "string"
    },
    "quantity": {
      "description": "The type(v) type is used.",
      "type": "number"
    },
    "product_price": {
      "description": "The type(v) type is used.",
      "type": "number"
    },
    "total_products": {
      "description": "The type(v) type is used.",
      "type": "number"
    }
  },
  "title": "SampleRecord",
  "type": "object"
}
    """


    schema_registry_conf = schema_config()
    schema_registry_client = SchemaRegistryClient(schema_registry_conf) #schema registry client object is created.



    #print(schema_registry_client)
    #topic = 'restaurent-take-away-data'
    #print(dir(schema_registry_client))
    #print('\n')
    #print(f'{schema_registry_client.get_subjects()=}')
    #print(f"""{schema_registry_client.get_latest_version('restaurent-take-away-data-value')}""")
    #print(dir(schema_registry_client.get_latest_version('restaurent-take-away-data-value')))

    #print(dir(schema_registry_client.get_latest_version('test_topic_1-value').schema))
    


    schema_x=schema_registry_client.get_latest_version('restaurent-take-away-data-value').schema.schema_str
    subject=schema_registry_client.get_subjects()
    #print(x)
    #y=schema_registry_client.get_subjects(subject).schema.schema_str




    string_serializer = StringSerializer('utf_8') #string serialize.it will be used to serialized our key.
    json_serializer = JSONSerializer(schema_x, schema_registry_client, car_to_dict)

    producer = Producer(sasl_conf())

    print("Producing user records to topic {}. ^C to exit.".format(topic))
    #while True:
        # Serve on_delivery callbacks from previous calls to produce()
    producer.poll(0.0)
    try:
        for car in get_car_instance(file_path=FILE_PATH):

            print(car)
            producer.produce(topic=topic,
                            key=string_serializer(str(uuid4()), car_to_dict),
                            value=json_serializer(car, SerializationContext(topic, MessageField.VALUE)),
                            on_delivery=delivery_report)
            #break
    except KeyboardInterrupt:
        pass
    except ValueError:
        print("Invalid input, discarding record...")
        pass

    print("\nFlushing records...")
    producer.flush()

main("restaurent-take-away-data")

  
  
  ```
  
## Two consumer with same group ID
Consumer1.py
```
import argparse

from confluent_kafka import Consumer
from confluent_kafka.serialization import SerializationContext, MessageField
from confluent_kafka.schema_registry.json_schema import JSONDeserializer
from confluent_kafka.schema_registry import SchemaRegistryClient



from confluent_kafka.schema_registry import SchemaRegistryClient
import datetime


API_KEY = '5EDWNFRIVKUNMNRV'
ENDPOINT_SCHEMA_URL  = 'https://psrc-vrpp5.us-east-2.aws.confluent.cloud'
API_SECRET_KEY = 'Fn7EquWlsmKgWmXkH6Jkpnz+3YDq4BHyktXyyrp1OYyeIvVEJmAYWFotXqnYKQ4m'
BOOTSTRAP_SERVER = 'pkc-6ojv2.us-west4.gcp.confluent.cloud:9092'
SECURITY_PROTOCOL = 'SASL_SSL'
SSL_MACHENISM = 'PLAIN'
SCHEMA_REGISTRY_API_KEY = 'JECJFMV6IMWVNMNL'
SCHEMA_REGISTRY_API_SECRET = 'xieywUy2MILvFQkq31gmRmkKnNsu4Xpd+X2WNzCzRZFpx7VxvVQTQQPOwHgyuqWV'


def sasl_conf():

    sasl_conf = {'sasl.mechanism': SSL_MACHENISM,
                 # Set to SASL_SSL to enable TLS support.
                #  'security.protocol': 'SASL_PLAINTEXT'}
                'bootstrap.servers':BOOTSTRAP_SERVER,
                'security.protocol': SECURITY_PROTOCOL,
                'sasl.username': API_KEY,
                'sasl.password': API_SECRET_KEY
                }
    return sasl_conf



def schema_config():
    return {'url':ENDPOINT_SCHEMA_URL,
    
    'basic.auth.user.info':f"{SCHEMA_REGISTRY_API_KEY}:{SCHEMA_REGISTRY_API_SECRET}"

    }


class Car:   
    def __init__(self,record:dict):
        for k,v in record.items():
            setattr(self,k,v)
        
        self.record=record
   
    @staticmethod
    def dict_to_car(data:dict,ctx):
        return Car(record=data)

    def __str__(self):
        return f"{self.record}"








def main(topic):



    schema_registry_conf = schema_config()
    schema_registry_client = SchemaRegistryClient(schema_registry_conf) #schema registry client object is created.



    schema_x=schema_registry_client.get_latest_version('restaurent-take-away-data-value').schema.schema_str

    json_deserializer = JSONDeserializer(schema_x,from_dict=Car.dict_to_car)

    consumer_conf = sasl_conf()
    consumer_conf.update({
                     'group.id': 'group1',
                     'auto.offset.reset': "earliest"})

    consumer = Consumer(consumer_conf)
    consumer.subscribe([topic])
    counter=0

    while True:
        try:
            # SIGINT can't be handled when polling, limit timeout to 1 second.
            msg = consumer.poll(1.0)
            if msg is None:
                continue

            car = json_deserializer(msg.value(), SerializationContext(msg.topic(), MessageField.VALUE))

            if car is not None:
                counter+=1
                print(datetime.datetime.now())
                print("User record {}: order: {}\n"
                          .format(msg.key(), car))

                  
                print('Total messages fetched till now:', counter)
        except KeyboardInterrupt:
            break

    consumer.close()

main("restaurent-take-away-data")
```
 Consumer2.py
 ```
 import argparse

from confluent_kafka import Consumer
from confluent_kafka.serialization import SerializationContext, MessageField
from confluent_kafka.schema_registry.json_schema import JSONDeserializer
from confluent_kafka.schema_registry import SchemaRegistryClient



from confluent_kafka.schema_registry import SchemaRegistryClient
import datetime


API_KEY = '5EDWNFRIVKUNMNRV'
ENDPOINT_SCHEMA_URL  = 'https://psrc-vrpp5.us-east-2.aws.confluent.cloud'
API_SECRET_KEY = 'Fn7EquWlsmKgWmXkH6Jkpnz+3YDq4BHyktXyyrp1OYyeIvVEJmAYWFotXqnYKQ4m'
BOOTSTRAP_SERVER = 'pkc-6ojv2.us-west4.gcp.confluent.cloud:9092'
SECURITY_PROTOCOL = 'SASL_SSL'
SSL_MACHENISM = 'PLAIN'
SCHEMA_REGISTRY_API_KEY = 'JECJFMV6IMWVNMNL'
SCHEMA_REGISTRY_API_SECRET = 'xieywUy2MILvFQkq31gmRmkKnNsu4Xpd+X2WNzCzRZFpx7VxvVQTQQPOwHgyuqWV'


def sasl_conf():

    sasl_conf = {'sasl.mechanism': SSL_MACHENISM,
                 # Set to SASL_SSL to enable TLS support.
                #  'security.protocol': 'SASL_PLAINTEXT'}
                'bootstrap.servers':BOOTSTRAP_SERVER,
                'security.protocol': SECURITY_PROTOCOL,
                'sasl.username': API_KEY,
                'sasl.password': API_SECRET_KEY
                }
    return sasl_conf



def schema_config():
    return {'url':ENDPOINT_SCHEMA_URL,
    
    'basic.auth.user.info':f"{SCHEMA_REGISTRY_API_KEY}:{SCHEMA_REGISTRY_API_SECRET}"

    }


class Car:   
    def __init__(self,record:dict):
        for k,v in record.items():
            setattr(self,k,v)
        
        self.record=record
   
    @staticmethod
    def dict_to_car(data:dict,ctx):
        return Car(record=data)

    def __str__(self):
        return f"{self.record}"








def main(topic):



    schema_registry_conf = schema_config()
    schema_registry_client = SchemaRegistryClient(schema_registry_conf) #schema registry client object is created.



    schema_x=schema_registry_client.get_latest_version('restaurent-take-away-data-value').schema.schema_str

    json_deserializer = JSONDeserializer(schema_x,from_dict=Car.dict_to_car)

    consumer_conf = sasl_conf()
    consumer_conf.update({
                     'group.id': 'group1',
                     'auto.offset.reset': "earliest"})

    consumer = Consumer(consumer_conf)
    consumer.subscribe([topic])
    counter=0

    while True:
        try:
            # SIGINT can't be handled when polling, limit timeout to 1 second.
            msg = consumer.poll(1.0)
            if msg is None:
                continue

            car = json_deserializer(msg.value(), SerializationContext(msg.topic(), MessageField.VALUE))

            if car is not None:
                counter+=1
                print(datetime.datetime.now())
                print("User record {}: order: {}\n"
                          .format(msg.key(), car))

                  
                print('Total messages fetched till now:', counter)
        except KeyboardInterrupt:
            break

    consumer.close()

main("restaurent-take-away-data")
 ```
  
  
## Two consumer with  different group ID .

Consumer1.py
```
import argparse

from confluent_kafka import Consumer
from confluent_kafka.serialization import SerializationContext, MessageField
from confluent_kafka.schema_registry.json_schema import JSONDeserializer
from confluent_kafka.schema_registry import SchemaRegistryClient



from confluent_kafka.schema_registry import SchemaRegistryClient
import datetime


API_KEY = '5EDWNFRIVKUNMNRV'
ENDPOINT_SCHEMA_URL  = 'https://psrc-vrpp5.us-east-2.aws.confluent.cloud'
API_SECRET_KEY = 'Fn7EquWlsmKgWmXkH6Jkpnz+3YDq4BHyktXyyrp1OYyeIvVEJmAYWFotXqnYKQ4m'
BOOTSTRAP_SERVER = 'pkc-6ojv2.us-west4.gcp.confluent.cloud:9092'
SECURITY_PROTOCOL = 'SASL_SSL'
SSL_MACHENISM = 'PLAIN'
SCHEMA_REGISTRY_API_KEY = 'JECJFMV6IMWVNMNL'
SCHEMA_REGISTRY_API_SECRET = 'xieywUy2MILvFQkq31gmRmkKnNsu4Xpd+X2WNzCzRZFpx7VxvVQTQQPOwHgyuqWV'


def sasl_conf():

    sasl_conf = {'sasl.mechanism': SSL_MACHENISM,
                 # Set to SASL_SSL to enable TLS support.
                #  'security.protocol': 'SASL_PLAINTEXT'}
                'bootstrap.servers':BOOTSTRAP_SERVER,
                'security.protocol': SECURITY_PROTOCOL,
                'sasl.username': API_KEY,
                'sasl.password': API_SECRET_KEY
                }
    return sasl_conf



def schema_config():
    return {'url':ENDPOINT_SCHEMA_URL,
    
    'basic.auth.user.info':f"{SCHEMA_REGISTRY_API_KEY}:{SCHEMA_REGISTRY_API_SECRET}"

    }


class Car:   
    def __init__(self,record:dict):
        for k,v in record.items():
            setattr(self,k,v)
        
        self.record=record
   
    @staticmethod
    def dict_to_car(data:dict,ctx):
        return Car(record=data)

    def __str__(self):
        return f"{self.record}"








def main(topic):



    schema_registry_conf = schema_config()
    schema_registry_client = SchemaRegistryClient(schema_registry_conf) #schema registry client object is created.



    schema_x=schema_registry_client.get_latest_version('restaurent-take-away-data-value').schema.schema_str

    json_deserializer = JSONDeserializer(schema_x,from_dict=Car.dict_to_car)

    consumer_conf = sasl_conf()
    consumer_conf.update({
                     'group.id': 'group1',
                     'auto.offset.reset': "earliest"})

    consumer = Consumer(consumer_conf)
    consumer.subscribe([topic])
    counter=0

    while True:
        try:
            # SIGINT can't be handled when polling, limit timeout to 1 second.
            msg = consumer.poll(1.0)
            if msg is None:
                continue

            car = json_deserializer(msg.value(), SerializationContext(msg.topic(), MessageField.VALUE))

            if car is not None:
                counter+=1
                print(datetime.datetime.now())
                print("User record {}: order: {}\n"
                          .format(msg.key(), car))

                  
                print('Total messages fetched till now:', counter)
        except KeyboardInterrupt:
            break

    consumer.close()

main("restaurent-take-away-data")



```

consumer2.py
```
import argparse

from confluent_kafka import Consumer
from confluent_kafka.serialization import SerializationContext, MessageField
from confluent_kafka.schema_registry.json_schema import JSONDeserializer
from confluent_kafka.schema_registry import SchemaRegistryClient



from confluent_kafka.schema_registry import SchemaRegistryClient

import datetime

API_KEY = '5EDWNFRIVKUNMNRV'
ENDPOINT_SCHEMA_URL  = 'https://psrc-vrpp5.us-east-2.aws.confluent.cloud'
API_SECRET_KEY = 'Fn7EquWlsmKgWmXkH6Jkpnz+3YDq4BHyktXyyrp1OYyeIvVEJmAYWFotXqnYKQ4m'
BOOTSTRAP_SERVER = 'pkc-6ojv2.us-west4.gcp.confluent.cloud:9092'
SECURITY_PROTOCOL = 'SASL_SSL'
SSL_MACHENISM = 'PLAIN'
SCHEMA_REGISTRY_API_KEY = 'JECJFMV6IMWVNMNL'
SCHEMA_REGISTRY_API_SECRET = 'xieywUy2MILvFQkq31gmRmkKnNsu4Xpd+X2WNzCzRZFpx7VxvVQTQQPOwHgyuqWV'


def sasl_conf():

    sasl_conf = {'sasl.mechanism': SSL_MACHENISM,
                 # Set to SASL_SSL to enable TLS support.
                #  'security.protocol': 'SASL_PLAINTEXT'}
                'bootstrap.servers':BOOTSTRAP_SERVER,
                'security.protocol': SECURITY_PROTOCOL,
                'sasl.username': API_KEY,
                'sasl.password': API_SECRET_KEY
                }
    return sasl_conf



def schema_config():
    return {'url':ENDPOINT_SCHEMA_URL,
    
    'basic.auth.user.info':f"{SCHEMA_REGISTRY_API_KEY}:{SCHEMA_REGISTRY_API_SECRET}"

    }


class Car:   
    def __init__(self,record:dict):
        for k,v in record.items():
            setattr(self,k,v)
        
        self.record=record
   
    @staticmethod
    def dict_to_car(data:dict,ctx):
        return Car(record=data)

    def __str__(self):
        return f"{self.record}"








def main(topic):



    schema_registry_conf = schema_config()
    schema_registry_client = SchemaRegistryClient(schema_registry_conf) #schema registry client object is created.



    schema_x=schema_registry_client.get_latest_version('restaurent-take-away-data-value').schema.schema_str

    json_deserializer = JSONDeserializer(schema_x,from_dict=Car.dict_to_car)

    consumer_conf = sasl_conf()
    consumer_conf.update({
                     'group.id': 'group2',
                     'auto.offset.reset': "earliest"})

    consumer = Consumer(consumer_conf)
    consumer.subscribe([topic])

    counter=0
    while True:
        try:
            # SIGINT can't be handled when polling, limit timeout to 1 second.
            msg = consumer.poll(1.0)
            if msg is None:
                continue

            car = json_deserializer(msg.value(), SerializationContext(msg.topic(), MessageField.VALUE))

            if car is not None:
                counter+=1
                print(datetime.datetime.now())
                print("User record {}: order: {}\n"
                          .format(msg.key(), car))

                  
                print('Total messages fetched till now:', counter)
        except KeyboardInterrupt:
            break

    consumer.close()

main("restaurent-take-away-data")
    
    
    
    
    
    
