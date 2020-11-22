package com.joelkell.demo.services.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.HttpStatus;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class UserHttpWrapper {
  private User user;
  private HttpStatus response;
  private String body;

  @BsonCreator
  @JsonCreator
  public UserHttpWrapper(
      @JsonProperty("reponse") @BsonProperty("reponse") HttpStatus response,
      @JsonProperty("user") @BsonProperty("user") User user,
      @JsonProperty("body") @BsonProperty("body") String body) {
    this.user = user;
    this.response = response;
    this.body = body;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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
