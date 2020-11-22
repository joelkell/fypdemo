package com.joelkell.demo.wrapper;

import com.joelkell.demo.product.Product;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.reactivex.Maybe;
import io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProductHttpWrapper {
  private Product product;
  private HttpStatus response;
  private String body;

  public ProductHttpWrapper(HttpResponse httpResponse) {
    if (httpResponse.getBody().get() instanceof String) {
      this.body = (String) httpResponse.getBody().get();
      ArrayList<String> categories = new ArrayList<>();
      categories.add("null");
      this.product = new Product("null", "mull", 0, new BigDecimal(0), categories);
    } else if (httpResponse.getBody().get() instanceof MaybeSwitchIfEmptySingle) {
      MaybeSwitchIfEmptySingle maybeSwitchIfEmptySingle =
          (MaybeSwitchIfEmptySingle) httpResponse.getBody().get();
      Object object = maybeSwitchIfEmptySingle.blockingGet();
      this.product = (Product) object;
      this.body = "null";
    } else if (httpResponse.getBody().get() instanceof Maybe) {
      Maybe maybe = (Maybe) httpResponse.getBody().get();
      Object object = maybe.blockingGet();
      this.product = (Product) object;
      this.body = "null";
    }
    this.response = httpResponse.getStatus();
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
