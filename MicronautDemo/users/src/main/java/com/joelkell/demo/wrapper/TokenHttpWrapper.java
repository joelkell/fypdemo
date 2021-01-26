package com.joelkell.demo.wrapper;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

public class TokenHttpWrapper {
  private BearerAccessRefreshToken bearerAccessRefreshToken;
  private HttpStatus response;
  private String body;

  public TokenHttpWrapper(HttpResponse httpResponse) {
    if (httpResponse.getBody().get() instanceof String) {
      this.body = (String) httpResponse.getBody().get();
      this.bearerAccessRefreshToken = new BearerAccessRefreshToken();
    } else if (httpResponse.getBody().get() instanceof BearerAccessRefreshToken) {
      this.bearerAccessRefreshToken = (BearerAccessRefreshToken) httpResponse.getBody().get();
      this.body = "null";
    }
    this.response = httpResponse.getStatus();
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
