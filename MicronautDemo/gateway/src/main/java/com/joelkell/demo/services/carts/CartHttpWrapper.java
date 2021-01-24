package com.joelkell.demo.services.carts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joelkell.demo.services.products.Product;
import io.micronaut.http.HttpStatus;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class CartHttpWrapper {
  private Cart cart;
  private HttpStatus response;
  private String body;

  @BsonCreator
  @JsonCreator
  public CartHttpWrapper(
      @JsonProperty("response") @BsonProperty("response") HttpStatus response,
      @JsonProperty("product") @BsonProperty("product") Product product,
      @JsonProperty("body") @BsonProperty("body") String body) {
    this.cart = cart;
    this.response = response;
    this.body = body;
  }

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
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
