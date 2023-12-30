package com.microservices.demo.kafka.to.elastic.service.consumer.impl;

import com.microservices.demo.config.KafkaConsumerConfigData;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {

  private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
  private final KafkaAdminClient kafkaAdminClient;
  private final KafkaConsumerConfigData kafkaConsumerConfigData;

  public TwitterKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                              KafkaAdminClient kafkaAdminClient,
                              KafkaConsumerConfigData kafkaConsumerConfigData) {
    this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    this.kafkaAdminClient = kafkaAdminClient;
    this.kafkaConsumerConfigData = kafkaConsumerConfigData;
  }

  @EventListener
  public void onAppStarted(ApplicationStartedEvent event) {
    kafkaAdminClient.checkTopicsCreated();
    log.info("Starting Consuming Messages...");
    Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer(
        kafkaConsumerConfigData.getConsumerGroupId())).start();
  }


  @Override
  @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
  public void receive(@Payload List<TwitterAvroModel> messages,
                      @Header(KafkaHeaders.RECEIVED_KEY) List<Long> keys,
                      @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

    log.info("""
                 {} number of message received with\s
                 keys : {},\s
                 partitions : {} and offsets : {}.\s
                 \n Sending it to elastic: Thread Id : {}
                 """, messages.size(), keys, partitions, offsets, Thread.currentThread().getId());

  }
}
