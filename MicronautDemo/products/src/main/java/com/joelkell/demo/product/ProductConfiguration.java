package com.joelkell.demo.product;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("products")
public class ProductConfiguration {

  private String databaseName = "micronautMicroservices";
  private String collectionName = "products";

  public String getDatabaseName() {
    return databaseName;
  }

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  public String getCollectionName() {
    return collectionName;
  }

  public void setCollectionName(String collectionName) {
    this.collectionName = collectionName;
  }
}
