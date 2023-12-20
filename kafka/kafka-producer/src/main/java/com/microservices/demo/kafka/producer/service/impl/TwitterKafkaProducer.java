package com.microservices.demo.kafka.producer.service.impl;

import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.producer.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TwitterKafkaProducer implements KafkaProducer<Long, TwitterAvroModel> {

  private final KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;

  public TwitterKafkaProducer(KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @PreDestroy
  public void close() {
    if (kafkaTemplate != null) {
      log.info("Closing kafka template...");
      kafkaTemplate.destroy();
    }
  }

  @Override
  public void send(String topicName, Long key, TwitterAvroModel message) {
    log.info("Sending message = '{}', to topic = '{}'", message, topicName);
    kafkaTemplate.send(topicName, key, message);
  }

}
