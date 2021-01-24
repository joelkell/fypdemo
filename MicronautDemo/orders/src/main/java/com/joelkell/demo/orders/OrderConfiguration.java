package com.joelkell.demo.orders;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("orders")
public class OrderConfiguration {

  private String databaseName = "micronautMicroservices";
  private String orderCollectionName = "orders";

  public String getDatabaseName() {
    return databaseName;
  }

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  public String getOrderCollectionName() {
    return orderCollectionName;
  }

  public void setOrderCollectionName(String cartCollectionName) {
    this.orderCollectionName = orderCollectionName;
  }
}
