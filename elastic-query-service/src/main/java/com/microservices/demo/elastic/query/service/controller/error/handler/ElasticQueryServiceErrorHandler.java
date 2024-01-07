package com.microservices.demo.elastic.query.service.controller.error.handler;

import com.microservices.demo.elastic.query.client.exception.ElasticQuerySearchException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ElasticQueryServiceErrorHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handle(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body("Illegal Argument Exception, "+e.getMessage());
  }

  @ExceptionHandler(ElasticQuerySearchException.class)
  public ResponseEntity<String> handle(ElasticQuerySearchException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handle(RuntimeException e) {
    return ResponseEntity.badRequest().body("Some error occurred, "+e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handle(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong! "+e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handle(MethodArgumentNotValidException e) {
    Map<String, String> map = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach(err -> map.put(((FieldError)err).getField(), err.getDefaultMessage()));
    return ResponseEntity.badRequest().body("Illegal Argument(s), "+map);
  }

}
