package com.joelkell.demo.reviews;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("reviews")
public class ReviewConfiguration {

  private String databaseName = "micronautMicroservices";
  private String collectionName = "reviews";

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
