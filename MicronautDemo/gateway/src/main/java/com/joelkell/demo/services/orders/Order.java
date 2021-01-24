package com.joelkell.demo.services.orders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joelkell.demo.json.ObjectIdJsonSerializer;
import com.joelkell.demo.validation.ListNotEmptyConstraint;
import com.joelkell.demo.validation.ObjectIdConstraint;
import com.joelkell.demo.validation.StatusConstraint;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.validation.Validated;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@BsonDiscriminator
@Introspected
@Validated
public class Order {

  @BsonId
  @JsonProperty("_id")
  @BsonProperty("_id")
  @JsonSerialize(using = ObjectIdJsonSerializer.class)
  private ObjectId id;

  @ObjectIdConstraint
  @JsonSerialize(using = ObjectIdJsonSerializer.class)
  private ObjectId userId;

  @StatusConstraint private Status status;

  @ListNotEmptyConstraint private List<OrderItem> orderItems;

  @NotNull @PositiveOrZero private BigDecimal totalPrice;

  private String timestamp;

  @BsonCreator
  @JsonCreator
  public Order(
      @JsonProperty("_id") @BsonProperty("_id") ObjectId id,
      @JsonProperty("userId") @BsonProperty("userId") ObjectId userId,
      @JsonProperty("status") @BsonProperty("status") Status status,
      @JsonProperty("totalPrice") @BsonProperty("totalPrice") BigDecimal totalPrice,
      @JsonProperty("timestamp") @BsonProperty("timestamp") String timestamp,
      @JsonProperty("orderItems") @BsonProperty("orderItems") ArrayList<OrderItem> orderItems) {
    this.id = id;
    this.userId = userId;
    this.status = status;
    this.totalPrice = totalPrice;
    this.timestamp = timestamp;
    if (this.orderItems == null && orderItems == null) {
      this.orderItems = new ArrayList<>();
    } else {
      this.orderItems = orderItems;
    }
  }

  public Order(
      ObjectId userId, Status status, BigDecimal totalPrice, ArrayList<OrderItem> orderItems) {
    this.userId = userId;
    this.status = status;
    this.totalPrice = totalPrice;
    this.orderItems = orderItems;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public ObjectId getUserId() {
    return userId;
  }

  public void setUserId(ObjectId userId) {
    this.userId = userId;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(userId).append(timestamp).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Order)) {
      return false;
    }
    Order o = (Order) obj;
    return new EqualsBuilder()
        .append(this.userId, o.userId)
        .append(this.timestamp, o.timestamp)
        .isEquals();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
