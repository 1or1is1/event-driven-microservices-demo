package com.microservices.demo.twitter.to.kafka.service.init.impl;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.twitter.to.kafka.service.init.StreamInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaStreamInitializer implements StreamInitializer {

  private final KafkaAdminClient kafkaAdminClient;
  private final KafkaConfigData kafkaConfigData;

  public KafkaStreamInitializer(KafkaAdminClient kafkaAdminClient, KafkaConfigData kafkaConfigData) {
    this.kafkaAdminClient = kafkaAdminClient;
    this.kafkaConfigData = kafkaConfigData;
  }


  @Override
  public void init() {
    kafkaAdminClient.createTopics();
    kafkaAdminClient.checkSchemaRegistry();
    log.info("Topics with name = [{}], are ready.!", kafkaConfigData.getTopicNamesToCreate());
  }

}
