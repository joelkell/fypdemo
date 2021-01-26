package com.joelkell.demo.services.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.HttpStatus;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class TokenHttpWrapper {
  private BearerAccessRefreshToken bearerAccessRefreshToken;
  private HttpStatus response;
  private String body;

  @BsonCreator
  @JsonCreator
  public TokenHttpWrapper(
      @JsonProperty("response") @BsonProperty("response") HttpStatus response,
      @JsonProperty("bearerAccessRefreshToken") @BsonProperty("bearerAccessRefreshToken") BearerAccessRefreshToken bearerAccessRefreshToken,
      @JsonProperty("body") @BsonProperty("body") String body) {
    this.bearerAccessRefreshToken = bearerAccessRefreshToken;
    this.response = response;
    this.body = body;
  }

  public BearerAccessRefreshToken getBearerAccessRefreshToken() {
    return bearerAccessRefreshToken;
  }

  public void setBearerAccessRefreshToken(BearerAccessRefreshToken bearerAccessRefreshToken) {
    this.bearerAccessRefreshToken = bearerAccessRefreshToken;
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
