package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class LittleLogConsumer<K, V> implements Runnable {

	private final KafkaConsumer<K, V> consumer;
	private final Class keyClass;
	private final Class valueClass;

	public LittleLogConsumer() {
		this("test", "localhost:9092", StringDeserializer.class, StringDeserializer.class);
	}

	public LittleLogConsumer(final String topic, final String bootstrapServers, final Class keyClass, final Class valueClass) {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyClass.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueClass.getName());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, topic);
		props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 150728640);
		this.keyClass = keyClass;
		this.valueClass = valueClass;
		this.consumer = new KafkaConsumer<>(props);
		this.consumer.subscribe(Collections.singleton(topic));
	}

	public LittleLogConsumer(final Properties props, final Class keyClass, final Class valueClass) {
		this.consumer = new KafkaConsumer<>(props);
		this.keyClass = keyClass;
		this.valueClass = valueClass;
	}

	@Override
	public void run() {
		while (true) {
			final ConsumerRecords<K, V> consumerRecords = this.consumer.poll(500);
//			if (consumerRecords.count() == 0) {
//				break;
//			}
			for (final ConsumerRecord<K, V> record : consumerRecords) {
				System.out.println(String.format("Consumer Record: (%s, %s, %d, %d)",
						record.key(), record.value(), record.partition(), record.offset()));
				System.out.println("Size: " + record.serializedKeySize() + "\nValue: " + record.serializedValueSize());
			}
			this.consumer.commitAsync();
		}
//		this.consumer.close();
//		System.out.println("Finished!");
	}
}
