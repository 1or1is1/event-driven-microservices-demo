package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.exception.ElasticQuerySearchException;
import com.microservices.demo.elastic.query.client.repository.TwitterElasticsearchQueryRepository;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwitterElasticRepositoryQueryClient implements ElasticQueryClient<TwitterIndexModel> {

  private final TwitterElasticsearchQueryRepository searchRepository;

  public TwitterElasticRepositoryQueryClient(TwitterElasticsearchQueryRepository searchRepository) {
    this.searchRepository = searchRepository;
  }

  @Override
  public TwitterIndexModel getIndexModelById(String id) {
    Optional<TwitterIndexModel> twitterIndexModelOptional = searchRepository.findById(id);
    if (twitterIndexModelOptional.isEmpty()) {
      throw new ElasticQuerySearchException(String.format("No document found with ID : %s", id));
    }
    log.info("Document with id : [{}], retrieved successfully", id);
    return twitterIndexModelOptional.get();
  }

  @Override
  public List<TwitterIndexModel> getIndexModelByText(String text) {
    List<TwitterIndexModel> twitterIndexModelList = searchRepository.findByText(text);
    log.info("Fetching Document(s) containing text : {}", text);
    return twitterIndexModelList;
  }

  @Override
  public List<TwitterIndexModel> getAllIndexModels() {
    log.info("Fetching all Documents");
    Iterable<TwitterIndexModel> indexModelsIterable = searchRepository.findAll();
    return getTwitterIndexModelsFromIterable(indexModelsIterable);
  }

  private static List<TwitterIndexModel> getTwitterIndexModelsFromIterable(
      Iterable<TwitterIndexModel> indexModelsIterable) {
    List<TwitterIndexModel> twitterIndexModels = new ArrayList<>();
    for (TwitterIndexModel model : indexModelsIterable) {
      twitterIndexModels.add(TwitterIndexModel.builder()
                                              .userId(model.getUserId())
                                              .id(model.getId())
                                              .text(model.getText())
                                              .createdAt(model.getCreatedAt())
                                              .build());
    }
    return twitterIndexModels;
  }

}
