package com.joelkell.demo.services.orders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.HttpStatus;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class OrderHttpWrapper {
  private Order order;
  private HttpStatus response;
  private String body;

  @BsonCreator
  @JsonCreator
  public OrderHttpWrapper(
      @JsonProperty("response") @BsonProperty("response") HttpStatus response,
      @JsonProperty("product") @BsonProperty("product") Order order,
      @JsonProperty("body") @BsonProperty("body") String body) {
    this.order = order;
    this.response = response;
    this.body = body;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public HttpStatus getResponse() {
    return response;
  }

  public void setResponse(HttpStatus response) {
    this.response = response;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
