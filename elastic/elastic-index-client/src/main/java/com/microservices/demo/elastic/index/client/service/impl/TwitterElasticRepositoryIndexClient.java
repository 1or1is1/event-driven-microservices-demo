package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.elastic.index.client.repository.TwitterElasticsearchIndexRepository;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {

  private final TwitterElasticsearchIndexRepository indexRepository;

  public TwitterElasticRepositoryIndexClient(TwitterElasticsearchIndexRepository indexRepository) {
    this.indexRepository = indexRepository;
  }


  @Override
  public List<String> save(List<TwitterIndexModel> documents) {
    List<TwitterIndexModel> twitterIndexModels = (List<TwitterIndexModel>) indexRepository.saveAll(documents);
    List<String> documentIds = twitterIndexModels.stream()
                                                 .map(TwitterIndexModel::getId)
                                                 .toList();
    log.info("Documents indexed (Repository) successfully with type : {} and IDs {}",
             TwitterElasticRepositoryIndexClient.class.getName(), documentIds);
    return documentIds;
  }
}
