package com.microservices.demo.twitter.to.kafka.service.runner.impl;

import com.microservices.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import jakarta.annotation.PreDestroy;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Slf4j
@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "false", matchIfMissing = true)
public class TwitterKafkaStreamRunner implements StreamRunner {

  private final TwitterToKafkaServiceConfigData configData;
  private final TwitterKafkaStatusListener twitterKafkaStatusListener;

  private TwitterStream twitterStream;

  public TwitterKafkaStreamRunner(TwitterToKafkaServiceConfigData configData,
      TwitterKafkaStatusListener kafkaStatusListener) {
    this.configData = configData;
    this.twitterKafkaStatusListener = kafkaStatusListener;
  }

  @PreDestroy
  public void shutdown() {
    twitterStream.shutdown();
  }

  @Override
  public void start() throws TwitterException {
    twitterStream = new TwitterStreamFactory().getInstance();
    twitterStream.addListener(twitterKafkaStatusListener);
    List<String> twitterKeywords = configData.getTwitterKeywords();
    log.info("Twitter Stream started for following filters : {}", twitterKeywords);
    twitterStream.filter();
  }

}
