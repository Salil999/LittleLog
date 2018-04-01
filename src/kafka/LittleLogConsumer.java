package kafka;

import littlelog.LittleLog;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Properties;

public class LittleLogConsumer<K, V> implements Runnable {

	public static final double ONE_MB = 1e6;
	private final KafkaConsumer<K, V> consumer;
	private final Class keyClass;
	private final Class valueClass;
	private final double chunkSize;

	public LittleLogConsumer() {
		this("test", "localhost:9092", 20.0 * ONE_MB, StringDeserializer.class, StringDeserializer
				.class);
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

	@Override
	public void run() {
		System.out.println("Consumer started!");
		try {
			FileWriter writer;
			File directory;
			long currentSize;
			int maxLogNumber = 0;
			final LittleLog littleLog = new LittleLog("compressed_logs/");
			final BufferedReader reader = new BufferedReader(new FileReader("../kafka_scripts/http.log"));
			String currLine;
			while ((currLine = reader.readLine()) != null) {
//				final ConsumerRecords<K, V> consumerRecords = this.consumer.poll(500);

				directory = new File("/Users/Salil/Desktop/LittleLog/src/logs/ll_" + maxLogNumber + ".log");
				if (!directory.exists()) {
					directory.getParentFile().mkdirs();
					directory.createNewFile();
				}

				writer = new FileWriter(directory, true);
				writer.write(currLine + "\n");

//				for (final ConsumerRecord<K, V> record : consumerRecords) {
//					final String key = record.key().toString();
//					final String value = record.value().toString();
//				    writer.write(String.format("%s, %s\n", key, value));
//				}

				writer.close();
				currentSize = directory.length();

				if (currentSize > this.chunkSize) {
					littleLog.compress("logs/ll_" + maxLogNumber + ".log");
					maxLogNumber++;

				}

				this.consumer.commitAsync();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
