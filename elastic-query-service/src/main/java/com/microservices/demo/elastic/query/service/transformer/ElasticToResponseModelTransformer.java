package com.microservices.demo.elastic.query.service.transformer;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryResponseModel;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ElasticToResponseModelTransformer {

  public ElasticQueryResponseModel getResponseModel(TwitterIndexModel model) {
    return ElasticQueryResponseModel.builder()
                                    .id(model.getId())
                                    .userId(model.getUserId())
                                    .text(model.getText())
                                    .createdAt(model.getCreatedAt())
                                    .build();
  }

  public List<ElasticQueryResponseModel> getResponseModels(List<TwitterIndexModel> models) {
    return models.stream().map(this::getResponseModel).toList();
  }

}
