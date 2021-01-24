package com.joelkell.demo.services.carts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joelkell.demo.json.ObjectIdJsonSerializer;
import com.joelkell.demo.validation.ObjectIdConstraint;
import io.micronaut.core.annotation.Introspected;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.validation.constraints.PositiveOrZero;

@BsonDiscriminator
@Introspected
public class CartItem {
  @ObjectIdConstraint
  @JsonSerialize(using = ObjectIdJsonSerializer.class)
  private ObjectId productId;

  @PositiveOrZero private int quantity;

  @BsonCreator
  @JsonCreator
  public CartItem(
      @JsonProperty("productId") @BsonProperty("productId") ObjectId productId,
      @JsonProperty("quantity") @BsonProperty("quantity") int quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public ObjectId getProductId() {
    return productId;
  }

  public void setProductId(ObjectId productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(productId).append(quantity).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CartItem)) {
      return false;
    }
    CartItem p = (CartItem) obj;
    return new EqualsBuilder()
        .append(this.productId, p.productId)
        .append(this.quantity, p.quantity)
        .isEquals();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
