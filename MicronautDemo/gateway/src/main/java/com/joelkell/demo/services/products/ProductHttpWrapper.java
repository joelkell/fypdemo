package com.joelkell.demo.services.products;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.HttpStatus;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class ProductHttpWrapper {
  private Product product;
  private HttpStatus response;
  private String body;

  @BsonCreator
  @JsonCreator
  public ProductHttpWrapper(
      @JsonProperty("response") @BsonProperty("response") HttpStatus response,
      @JsonProperty("product") @BsonProperty("product") Product product,
      @JsonProperty("body") @BsonProperty("body") String body) {
    this.product = product;
    this.response = response;
    this.body = body;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
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
