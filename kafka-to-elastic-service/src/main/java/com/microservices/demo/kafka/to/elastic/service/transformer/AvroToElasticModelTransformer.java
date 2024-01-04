package com.microservices.demo.kafka.to.elastic.service.transformer;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AvroToElasticModelTransformer {

  public List<TwitterIndexModel> getElasticModels(List<TwitterAvroModel> twitterAvroModels) {

    return twitterAvroModels.stream()
                            .map(twitterAvroModel ->
                                     TwitterIndexModel.builder()
                                                      .id(String.valueOf(twitterAvroModel.getId()))
                                                      .text(twitterAvroModel.getText())
                                                      .userId(twitterAvroModel.getUserId())
                                                      .createdAt(
                                                          LocalDateTime.ofInstant(
                                                              Instant.ofEpochMilli(
                                                                  twitterAvroModel.getCreatedAt()),
                                                              ZoneId.systemDefault())
                                                      )
                                                      .build()
                            )
                            .toList();
  }

  private Date getDateFromLocalDate(TwitterAvroModel twitterAvroModel) {
    return Date.from(
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(
                twitterAvroModel.getCreatedAt()),
            ZoneId.systemDefault())
                     .atZone(ZoneId.systemDefault())
                     .toInstant());
  }

}
