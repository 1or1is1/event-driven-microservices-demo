package com.microservices.demo.elastic.query.client.exception;

public class ElasticQuerySearchException extends RuntimeException{

  public ElasticQuerySearchException(String message) {
    super(message);
  }

  public ElasticQuerySearchException(String message, Throwable t) {
    super(message, t);
  }

}
