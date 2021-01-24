package com.joelkell.demo.services.carts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joelkell.demo.json.ObjectIdJsonSerializer;
import com.joelkell.demo.validation.ObjectIdConstraint;
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

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@BsonDiscriminator
@Introspected
@Validated
public class Cart {

  @BsonId
  @JsonProperty("_id")
  @BsonProperty("_id")
  @JsonSerialize(using = ObjectIdJsonSerializer.class)
  private ObjectId id;

  @ObjectIdConstraint
  @JsonSerialize(using = ObjectIdJsonSerializer.class)
  private ObjectId userId;

  @PositiveOrZero private BigDecimal deliveryPrice;

  @PositiveOrZero private BigDecimal totalPrice;

  @Valid private List<CartItem> cartItems;

  @BsonCreator
  @JsonCreator
  public Cart(
      @JsonProperty("_id") @BsonProperty("_id") ObjectId id,
      @JsonProperty("userId") @BsonProperty("userId") ObjectId userId,
      @JsonProperty("deliveryPrice") @BsonProperty("deliveryPrice") BigDecimal deliveryPrice,
      @JsonProperty("password") @BsonProperty("password") BigDecimal totalPrice,
      @JsonProperty("cartItems") @BsonProperty("cartItems") ArrayList<CartItem> cartItems) {
    this.id = id;
    this.userId = userId;
    this.deliveryPrice = deliveryPrice;
    this.totalPrice = totalPrice;
    if (this.cartItems == null && cartItems == null) {
      this.cartItems = new ArrayList<CartItem>();
    } else {
      this.cartItems = cartItems;
    }
  }

  public Cart(ObjectId userId) {
    this.userId = userId;
    if (this.cartItems == null) {
      this.cartItems = new ArrayList<CartItem>();
    }
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

  public BigDecimal getDeliveryPrice() {
    return deliveryPrice;
  }

  public void setDeliveryPrice(BigDecimal deliveryPrice) {
    this.deliveryPrice = deliveryPrice;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public List<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItem> cartItems) {
    this.cartItems = cartItems;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(userId).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Cart)) {
      return false;
    }
    Cart c = (Cart) obj;
    return new EqualsBuilder().append(this.userId, c.userId).isEquals();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
