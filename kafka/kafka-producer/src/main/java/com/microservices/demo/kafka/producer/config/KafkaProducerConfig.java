package com.microservices.demo.kafka.producer.config;

import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaProducerConfigData;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

@Slf4j
@Configuration
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {

  private final KafkaConfigData kafkaConfigData;
  private final KafkaProducerConfigData kafkaProducerConfigData;

  public KafkaProducerConfig(KafkaConfigData kafkaConfigData,
                             KafkaProducerConfigData kafkaProducerConfigData) {
    this.kafkaConfigData = kafkaConfigData;
    this.kafkaProducerConfigData = kafkaProducerConfigData;
  }

  @Bean
  public Map<String, Object> producerConfig() {
    Map<String, Object> map = new HashMap<>();
    map.put(BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
    map.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
    map.put(KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getKeySerializerClass());
    map.put(VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getValueSerializerClass());
    map.put(BATCH_SIZE_CONFIG, kafkaProducerConfigData.getBatchSize()
        * kafkaProducerConfigData.getBatchSizeBoostFactor());
    map.put(LINGER_MS_CONFIG, kafkaProducerConfigData.getLingerMs());
    map.put(COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.getCompressionType());
    map.put(ACKS_CONFIG, kafkaProducerConfigData.getAcks());
    map.put(REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerConfigData.getRequestTimeoutMs());
    map.put(RETRIES_CONFIG, kafkaProducerConfigData.getRetryCount());

    return map;
  }

  @Bean
  public ProducerFactory<K, V> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfig());
  }

  @Bean
  public KafkaTemplate<K, V> kafkaTemplate() {
    KafkaTemplate<K, V> kafkaTemplate = new KafkaTemplate<>(producerFactory());
    kafkaTemplate.setProducerListener(new ProducerListener<>() {
      @Override
      public void onSuccess(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata) {
        log.debug(
            """
            Received new metadata...
            Topic : '{}'
            Partition : '{}'
            Offset : '{}'
            Timestamp : '{}'
            at time '{}';
            """,
            recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),
            recordMetadata.timestamp(), System.nanoTime());
      }

      @Override
      public void onError(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata,
                          Exception exception) {
        log.error("Error while sending message '{}', to topic '{}'", producerRecord.value(),
                  producerRecord.topic());
      }
    });
    return kafkaTemplate;
  }

}
