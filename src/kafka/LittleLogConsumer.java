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

	public LittleLogConsumer() {
		this("test", "localhost:9092", StringDeserializer.class, StringDeserializer.class);
	}

	public LittleLogConsumer(final Properties props, final Class keyClass, final Class valueClass) {
		this.consumer = new KafkaConsumer<>(props);
		this.keyClass = keyClass;
		this.valueClass = valueClass;
	}

	public LittleLogConsumer(final String topic, final String bootstrapServers, final Class keyClass, final Class valueClass) {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyClass.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueClass.getName());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, topic);
		this.keyClass = keyClass;
		this.valueClass = valueClass;
		this.consumer = new KafkaConsumer<>(props);
		this.consumer.subscribe(Collections.singleton(topic));
	}

	@Override
	public void run() {
		try {
			FileWriter writer;
			File directory;
			long currentSize = 0;
			final int maxLogNumber = 0;
			while (true) {
				final ConsumerRecords<K, V> consumerRecords = this.consumer.poll(500);

				directory = new File("/Users/Salil/Desktop/LittleLog/logs/ll-" + maxLogNumber + ".log");
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
				if (currentSize > 1e7) {
//				maxLogNumber++;
					break;
				}
				this.consumer.commitAsync();
			}
			this.consumer.close();
			System.out.println("Finished!");
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
