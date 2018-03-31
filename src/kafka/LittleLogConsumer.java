package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Properties;

public class LittleLogConsumer<K, V> implements Runnable {

	private final KafkaConsumer<K, V> consumer;
	private final Class keyClass;
	private final Class valueClass;
	private final double chunkSize;

	public LittleLogConsumer() {
		this("test", "localhost:9092", 1e9, StringDeserializer.class, StringDeserializer.class);
	}

	public LittleLogConsumer(final Properties props, final Class keyClass, final double chunkSize, final Class
			valueClass) {
		this.consumer = new KafkaConsumer<>(props);
		this.keyClass = keyClass;
		this.valueClass = valueClass;
		this.chunkSize = chunkSize;
	}

	public LittleLogConsumer(final String topic, final String bootstrapServers, final double chunkSize, final Class
			keyClass, final Class valueClass) {
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
		this.chunkSize = chunkSize;
	}

	public LittleLogConsumer(final Properties props, final Class keyClass, final Class valueClass) {
		this.consumer = new KafkaConsumer<>(props);
		this.keyClass = keyClass;
		this.valueClass = valueClass;
	}

	@Override
	public void run() {
		System.out.println("Consumer started!");
		try {
			FileWriter writer;
			File directory;
			long currentSize;
			int maxLogNumber = 0;
			while (true) {
				final ConsumerRecords<K, V> consumerRecords = this.consumer.poll(500);

				directory = new File("/Users/Salil/Desktop/LittleLog/logs/ll_" + maxLogNumber + ".log");
				if (!directory.exists()) {
					directory.getParentFile().mkdirs();
					directory.createNewFile();
				}

				writer = new FileWriter(directory, true);
				for (final ConsumerRecord<K, V> record : consumerRecords) {
					final String key = record.key().toString();
					final String value = record.value().toString();
					writer.write(String.format("%s, %s\n", key, value));
				}

				writer.close();
				currentSize = directory.length();
				if (currentSize > this.chunkSize) {
					maxLogNumber++;
				}
				this.consumer.commitAsync();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
