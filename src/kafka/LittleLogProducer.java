package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class LittleLogProducer implements Runnable {

  private final KafkaProducer<String, String> producer;
  private final Class keyClass;
  private final Class valueClass;

  public LittleLogProducer() throws Exception {
    this("test", "localhost:9092", StringSerializer.class, StringSerializer.class);
  }

  public LittleLogProducer(final Properties props, final Class keyClass, final Class valueClass) {
    this.producer = new KafkaProducer<>(props);
    this.keyClass = keyClass;
    this.valueClass = valueClass;
  }

  public LittleLogProducer(
      final String topic,
      final String bootstrapServers,
      final Class keyClass,
      final Class valueClass) {
    final Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keyClass.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueClass.getName());
    this.keyClass = keyClass;
    this.valueClass = valueClass;
    this.producer = new KafkaProducer<>(props);
  }

  @Override
  public void run() {
    while (true) {
      final ProducerRecord<String, String> record = new ProducerRecord<>("test", "KEY", "VALUE");
      try {
        this.producer.send(record).get();
      } catch (final Exception e) {
        System.out.println("ERROR");
      }
      //      System.out.println(
      //          String.format("ProducerRecord(key=%s, value=%s", record.key(), record.value()));
    }
  }
}
