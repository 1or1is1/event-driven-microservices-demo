package com.microservices.demo.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "twitter-to-kafka-service")
public class TwitterToKafkaServiceConfigData {

  private List<String> twitterKeywords;
  private boolean enableMockTweets;
  private int mockTweetMinLength;
  private int mockTweetMaxLength;
  private long mockSleepMs;

}
