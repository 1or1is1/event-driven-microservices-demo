package com.microservices.demo.elastic.index.client.util;

import com.microservices.demo.elastic.model.index.IndexModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class ElasticIndexUtil<T extends IndexModel> {

  public List<IndexQuery> getIndexQuery(List<T> documents) {
    return documents.stream()
                    .map(document -> new IndexQueryBuilder().withId(document.getId())
                                                            .withObject(document).build())
                    .collect(Collectors.toList());
  }

}
