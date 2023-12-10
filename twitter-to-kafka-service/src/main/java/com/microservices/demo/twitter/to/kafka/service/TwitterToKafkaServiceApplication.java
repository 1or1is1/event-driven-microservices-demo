package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import twitter4j.TwitterException;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.microservices.demo.config"})
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

  private final TwitterToKafkaServiceConfigData configData;
  private final StreamRunner streamRunner;

  public TwitterToKafkaServiceApplication(
      TwitterToKafkaServiceConfigData configData,
      StreamRunner streamRunner) {
    this.configData = configData;
    this.streamRunner = streamRunner;
  }

  public static void main(String[] args) {
    SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
  }

  @Override
  public void run(String... args) {
    try {
      streamRunner.start();
    } catch (TwitterException e) {
      throw new RuntimeException(e);
    }
  }
}
