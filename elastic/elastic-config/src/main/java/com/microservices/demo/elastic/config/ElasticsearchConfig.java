package com.microservices.demo.elastic.config;

import com.microservices.demo.config.ElasticConfigData;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.support.HttpHeaders;
import org.springframework.lang.NonNull;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.microservices.demo.elastic")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

  private final ElasticConfigData elasticConfigData;

  public ElasticsearchConfig(ElasticConfigData elasticConfigData) {
    this.elasticConfigData = elasticConfigData;
  }

  @Override
  @NonNull
  public ClientConfiguration clientConfiguration() {
    HttpHeaders defaultHeaders = new HttpHeaders();
    defaultHeaders.add("Content-Type", "application/json");
    return ClientConfiguration.builder()
                              .connectedTo(elasticConfigData.getConnectionUrl())
                              .withDefaultHeaders(defaultHeaders)
                              .withConnectTimeout(elasticConfigData.getConnectionTimeoutMs())
                              .withSocketTimeout(elasticConfigData.getSocketTimeoutMs())
                              .build();
  }
}
