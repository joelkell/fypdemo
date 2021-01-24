package com.joelkell.demo.carts;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("carts")
public class CartConfiguration {

  private String databaseName = "micronautMicroservices";
  private String cartCollectionName = "carts";

  public String getDatabaseName() {
    return databaseName;
  }

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  public String getCartCollectionName() {
    return cartCollectionName;
  }

  public void setCartCollectionName(String cartCollectionName) {
    this.cartCollectionName = cartCollectionName;
  }
}
