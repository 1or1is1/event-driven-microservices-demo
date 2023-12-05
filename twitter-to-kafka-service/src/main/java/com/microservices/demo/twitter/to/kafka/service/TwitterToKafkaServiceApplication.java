package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

  private final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
  private final TwitterToKafkaServiceConfigData configData;

  public TwitterToKafkaServiceApplication(TwitterToKafkaServiceConfigData configData) {
    this.configData = configData;
  }

  public static void main(String[] args) {
    SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
  }

  @Override
  public void run(String... args) {
    LOG.info("Twitter Keywords : {}", configData.getTwitterKeywords());
  }
}
