package com.joelkell.demo.user;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("users")
public class UserConfiguration {

  private String databaseName = "micronautMicroservices";
  private String collectionName = "users";

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
