package com.joelkell.demo.services.reviews;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.HttpStatus;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class ReviewHttpWrapper {
  private Review review;
  private HttpStatus response;
  private String body;

  @BsonCreator
  @JsonCreator
  public ReviewHttpWrapper(
      @JsonProperty("response") @BsonProperty("response") HttpStatus response,
      @JsonProperty("review") @BsonProperty("review") Review review,
      @JsonProperty("body") @BsonProperty("body") String body) {
    this.review = review;
    this.response = response;
    this.body = body;
  }

  public Review getReview() {
    return review;
  }

  public void setReview(Review review) {
    this.review = review;
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
