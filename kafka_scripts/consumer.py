from kafka import KafkaConsumer
import time

if __name__ == '__main__':
    consumer = KafkaConsumer('test', bootstrap_servers='localhost:9092')
    while(True):
        for msg in consumer:
            print(msg)
        time.sleep(1)
