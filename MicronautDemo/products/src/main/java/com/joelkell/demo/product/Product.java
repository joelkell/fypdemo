package com.joelkell.demo.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joelkell.demo.json.ObjectIdJsonSerializer;
import io.micronaut.core.annotation.Introspected;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@BsonDiscriminator
@Introspected
public class Product {
  @BsonId
  @JsonProperty("_id")
  @BsonProperty("_id")
  @JsonSerialize(using = ObjectIdJsonSerializer.class)
  private ObjectId id;

  @NotBlank private String name;

  @NotBlank private String description;

  @NotNull @PositiveOrZero private int stockLevel;
  @NotNull @Positive private BigDecimal price;

  private List<String> categories;

  @BsonCreator
  @JsonCreator
  public Product(
      @JsonProperty("_id") @BsonProperty("_id") ObjectId id,
      @JsonProperty("name") @BsonProperty("name") String name,
      @JsonProperty("description") @BsonProperty("description") String description,
      @JsonProperty("stockLevel") @BsonProperty("stockLevel") int stockLevel,
      @JsonProperty("price") @BsonProperty("price") BigDecimal price,
      @JsonProperty("categories") @BsonProperty("categories") ArrayList<String> categories) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.stockLevel = stockLevel;
    this.price = price;
    if (this.categories == null && categories == null) {
      this.categories = new ArrayList<String>();
    } else {
      this.categories = categories;
    }
  }

  public Product(
      String name,
      String description,
      int stockLevel,
      BigDecimal price,
      ArrayList<String> categories) {
    this.name = name;
    this.description = description;
    this.stockLevel = stockLevel;
    this.price = price;
    this.categories = categories;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getStockLevel() {
    return stockLevel;
  }

  public void setStockLevel(int stockLevel) {
    this.stockLevel = stockLevel;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(name).append(description).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Product)) {
      return false;
    }
    Product p = (Product) obj;
    return new EqualsBuilder()
        .append(this.name, p.name)
        .append(this.description, p.description)
        .isEquals();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
