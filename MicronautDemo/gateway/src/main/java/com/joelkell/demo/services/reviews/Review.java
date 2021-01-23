package com.joelkell.demo.services.reviews;

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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@BsonDiscriminator
@Validated
@Introspected
public class Review {

    @BsonId
    @JsonProperty("_id")
    @BsonProperty("_id")
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId id;

    @ObjectIdConstraint
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId productId;

    @ObjectIdConstraint
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId userId;

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank private String comment;

    @BsonCreator
    @JsonCreator
    public Review(
            @JsonProperty("_id") @BsonProperty("_id") ObjectId id,
            @JsonProperty("productId") @BsonProperty("productId") ObjectId productId,
            @JsonProperty("userId") @BsonProperty("userId") ObjectId userId,
            @JsonProperty("rating") @BsonProperty("rating") int rating,
            @JsonProperty("comment") @BsonProperty("comment") String comment) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    public Review(ObjectId productId, ObjectId userId, int rating, String comment) {
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
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

    public ObjectId getProductId() {
        return productId;
    }

    public void setProductId(ObjectId productId) {
        this.productId = productId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(userId).append(productId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Review)) {
            return false;
        }
        Review p = (Review) obj;
        return new EqualsBuilder()
                .append(this.userId, p.userId)
                .append(this.productId, p.productId)
                .isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
