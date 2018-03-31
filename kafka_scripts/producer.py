from kafka import KafkaProducer
import time


if __name__ == '__main__':
    producer = KafkaProducer(bootstrap_servers='localhost:9092', max_request_size=150728640)
    while(True):
        producer.send('test', key=bytes(str(time.time()).encode('utf-8')), value=b'hello world!')
        time.sleep(1)
