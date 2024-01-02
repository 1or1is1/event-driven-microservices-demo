package com.microservices.demo.twitter.to.kafka.service.runner.impl;

import com.microservices.demo.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

@Slf4j
@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
public class MockKafkaStreamRunner implements StreamRunner {

  private final TwitterToKafkaServiceConfigData configData;
  private final TwitterKafkaStatusListener statusListener;

  @SuppressWarnings("SpellCheckingInspection")
  private final String[] words = ("amet luctus venenatis lectus magna fringilla urna porttitor "
      + "rhoncus dolor purus non enim praesent elementum facilisis leo vel fringilla est "
      + "ullamcorper eget nulla facilisi etiam").split(" ");

  private static final String rawJsonTweet =
      "{" + "\"created_at\" : \"{0}\"," + "\"id\" : \"{1}\"," + "\"text\" : \"{2}\","
          + "\"user\" : {" + "\"id\" : \"{3}\"" + "}}";

  public MockKafkaStreamRunner(TwitterToKafkaServiceConfigData configData,
                               TwitterKafkaStatusListener statusListener) {
    this.configData = configData;
    this.statusListener = statusListener;
  }


  @Override
  public void start() {
    String[] keywords = configData.getTwitterKeywords().toArray(new String[0]);
    int minTweetLength = configData.getMockTweetMinLength();
    int maxTweetLength = configData.getMockTweetMaxLength();
    long sleepTime = configData.getMockSleepMs();
    log.info("Starting twitter MOCK filtering stream...");
    log.info("Keywords : {}", Arrays.toString(keywords));
    simulateRandomTweet(maxTweetLength, minTweetLength, sleepTime);
  }

  private void simulateRandomTweet(int maxTweetLength, int minTweetLength, long sleepTime) {
    Executors.newSingleThreadExecutor().submit(() -> {
      try {
        while (true) {
          String tweet = getRandomTweet(maxTweetLength, minTweetLength);
          Status status = TwitterObjectFactory.createStatus(tweet);
          statusListener.onStatus(status);
          sleep(sleepTime);
        }
      }
      catch (TwitterException e) {
        log.error("Some Error Occurred", e);
      }
    });
  }

  private static void sleep(long sleepTime) {
    try {
      Thread.sleep(sleepTime);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private String getRandomTweet(int maxTweetLength, int minTweetLength) {
    String randomTweet = generateRandomTweetText(maxTweetLength, minTweetLength);
    return getFormattedTwitterStatusText(randomTweet);
  }

  private String getFormattedTwitterStatusText(String randomTweet) {
    String[] rawJsonTweetParams = new String[]{
        ZonedDateTime.now().format(
            DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH)),
        String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)), randomTweet,
        String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))};
    String tweet = rawJsonTweet;
    for (int i = 0; i < rawJsonTweetParams.length; i++) {
      tweet = tweet.replace("{" + i + "}", rawJsonTweetParams[i]);
    }
    return tweet;
  }

  private String generateRandomTweetText(int maxTweetLength, int minTweetLength) {
    int tweetLength = Math.max(new Random().nextInt(maxTweetLength + 1), minTweetLength);
    StringJoiner joiner = new StringJoiner(" ");
    int bound = tweetLength + 1;
    for (int i = 0; i < bound; i++) {
      String word = words[new Random().nextInt(words.length)];
      joiner.add(word);
    }
    return joiner.toString();
  }

}
