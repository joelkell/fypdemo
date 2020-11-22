package com.joelkell.demo.user;

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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@BsonDiscriminator
@Introspected
public class User {

  @BsonId
  @JsonProperty("_id")
  @BsonProperty("_id")
  @JsonSerialize(using = ObjectIdJsonSerializer.class)
  private ObjectId id;

  @NotBlank
  @Pattern(regexp = "([a-z][a-zA-Z0-9_-]{7,29}+$)")
  private String username;

  @NotBlank @Email private String email;

  @NotBlank private String password;

  @BsonCreator
  @JsonCreator
  public User(
      @JsonProperty("_id") @BsonProperty("_id") ObjectId id,
      @JsonProperty("username") @BsonProperty("username") String username,
      @JsonProperty("email") @BsonProperty("email") String email,
      @JsonProperty("password") @BsonProperty("password") String password) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(username).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof User)) {
      return false;
    }
    User p = (User) obj;
    return new EqualsBuilder().append(this.username, p.username).isEquals();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
