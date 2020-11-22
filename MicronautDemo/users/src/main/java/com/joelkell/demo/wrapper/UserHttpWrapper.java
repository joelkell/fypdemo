package com.joelkell.demo.wrapper;

import com.joelkell.demo.user.User;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.reactivex.Maybe;
import io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle;

public class UserHttpWrapper {
  private User user;
  private HttpStatus response;
  private String body;

  public UserHttpWrapper(HttpResponse httpResponse) {
    if (httpResponse.getBody().get() instanceof String) {
      this.body = (String) httpResponse.getBody().get();
      this.user = new User("null", "mull", "null");
    } else if (httpResponse.getBody().get() instanceof MaybeSwitchIfEmptySingle) {
      MaybeSwitchIfEmptySingle maybeSwitchIfEmptySingle =
          (MaybeSwitchIfEmptySingle) httpResponse.getBody().get();
      Object object = maybeSwitchIfEmptySingle.blockingGet();
      this.user = (User) object;
      this.body = "null";
    } else if (httpResponse.getBody().get() instanceof Maybe) {
      Maybe maybe = (Maybe) httpResponse.getBody().get();
      Object object = maybe.blockingGet();
      this.user = (User) object;
      this.body = "null";
    }
    this.response = httpResponse.getStatus();
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
