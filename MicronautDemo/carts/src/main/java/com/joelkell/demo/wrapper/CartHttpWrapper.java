package com.joelkell.demo.wrapper;

import com.joelkell.demo.carts.Cart;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.reactivex.Maybe;
import io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle;
import org.bson.types.ObjectId;

public class CartHttpWrapper {
  private Cart cart;
  private HttpStatus response;
  private String body;

  public CartHttpWrapper(HttpResponse httpResponse) {
    if (httpResponse.getBody().get() instanceof String) {
      this.body = (String) httpResponse.getBody().get();
      this.cart = new Cart(new ObjectId("507f1f77bcf86cd799439011"));
    } else if (httpResponse.getBody().get() instanceof MaybeSwitchIfEmptySingle) {
      MaybeSwitchIfEmptySingle maybeSwitchIfEmptySingle =
          (MaybeSwitchIfEmptySingle) httpResponse.getBody().get();
      Object object = maybeSwitchIfEmptySingle.blockingGet();
      this.cart = (Cart) object;
      this.body = "null";
    } else if (httpResponse.getBody().get() instanceof Maybe) {
      Maybe maybe = (Maybe) httpResponse.getBody().get();
      Object object = maybe.blockingGet();
      this.cart = (Cart) object;
      this.body = "null";
    }
    this.response = httpResponse.getStatus();
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
