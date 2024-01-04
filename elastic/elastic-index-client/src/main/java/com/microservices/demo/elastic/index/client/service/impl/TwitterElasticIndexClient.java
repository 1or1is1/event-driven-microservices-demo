package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.config.ElasticConfigData;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.index.client.util.ElasticIndexUtil;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "false")
public class TwitterElasticIndexClient implements ElasticIndexClient<TwitterIndexModel> {

  private final ElasticConfigData elasticConfigData;
  private final ElasticsearchOperations elasticsearchOperations;
  private final ElasticIndexUtil<TwitterIndexModel> elasticIndexUtil;


  public TwitterElasticIndexClient(ElasticConfigData elasticConfigData,
                                   ElasticsearchOperations elasticsearchOperations,
                                   ElasticIndexUtil<TwitterIndexModel> elasticIndexUtil) {
    this.elasticConfigData = elasticConfigData;
    this.elasticsearchOperations = elasticsearchOperations;
    this.elasticIndexUtil = elasticIndexUtil;
  }


  @Override
  public List<String> save(List<TwitterIndexModel> documents) {
    List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQuery(documents);
    List<String> documentIds = elasticsearchOperations
        .bulkIndex(indexQueries, IndexCoordinates.of(elasticConfigData.getIndexName()))
        .stream()
        .map(IndexedObjectInformation::id)
        .toList();
    log.info("Documents indexed successfully with type : {} and IDs {}",
             TwitterIndexModel.class.getName(), documentIds);
    return documentIds;
  }
}
