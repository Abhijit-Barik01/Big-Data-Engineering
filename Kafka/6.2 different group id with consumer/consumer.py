import argparse

from confluent_kafka import Consumer
from confluent_kafka.serialization import SerializationContext, MessageField
from confluent_kafka.schema_registry.json_schema import JSONDeserializer
from confluent_kafka.schema_registry import SchemaRegistryClient



from confluent_kafka.schema_registry import SchemaRegistryClient



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
