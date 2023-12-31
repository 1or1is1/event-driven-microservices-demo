package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.twitter.to.kafka.service.init.StreamInitializer;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import twitter4j.TwitterException;

@SpringBootApplication
@ComponentScan(basePackages = {"com.microservices.demo"})
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

  private final StreamRunner streamRunner;
  private final StreamInitializer streamInitializer;

  public TwitterToKafkaServiceApplication(StreamRunner streamRunner,
                                          StreamInitializer streamInitializer) {
    this.streamRunner = streamRunner;
    this.streamInitializer = streamInitializer;
  }

  public static void main(String[] args) {
    SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
  }

  @Override
  public void run(String... args) {
    try {
      streamInitializer.init();
      streamRunner.start();
    } catch (TwitterException e) {
      throw new RuntimeException(e);
    }
  }
}
