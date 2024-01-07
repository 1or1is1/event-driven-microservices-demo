package com.microservices.demo.elastic.query.service.controller;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.model.ElasticQueryRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryResponseModel;
import com.microservices.demo.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents")
public class ElasticDocumentController {

  private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
  private final ElasticToResponseModelTransformer modelTransformer;

  public ElasticDocumentController(ElasticQueryClient<TwitterIndexModel> elasticQueryClient,
                                   ElasticToResponseModelTransformer modelTransformer) {
    this.elasticQueryClient = elasticQueryClient;
    this.modelTransformer = modelTransformer;
  }

  @GetMapping("/{id}")
  public ResponseEntity<ElasticQueryResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
    TwitterIndexModel model = elasticQueryClient.getIndexModelById(id);
    ElasticQueryResponseModel responseModel = modelTransformer.getResponseModel(model);
    return ResponseEntity.ok(responseModel);
  }

  @GetMapping("")
  public ResponseEntity<List<ElasticQueryResponseModel>> getAllDocuments() {
    List<TwitterIndexModel> allIndexModels = elasticQueryClient.getAllIndexModels();
    List<ElasticQueryResponseModel> elasticQueryResponseModels = modelTransformer.getResponseModels(
        allIndexModels);
    return ResponseEntity.ok(elasticQueryResponseModels);
  }

  @PostMapping("/get-document-by-text")
  public ResponseEntity<List<ElasticQueryResponseModel>> getDocumentByText(
      @RequestBody @Valid ElasticQueryRequestModel elasticQueryRequestModel) {
    List<TwitterIndexModel> indexModelsByText = elasticQueryClient.getIndexModelByText(
        elasticQueryRequestModel.getText());

    List<ElasticQueryResponseModel> elasticQueryResponseModels = modelTransformer.getResponseModels(
        indexModelsByText);
    return ResponseEntity.ok(elasticQueryResponseModels);

  }

}
