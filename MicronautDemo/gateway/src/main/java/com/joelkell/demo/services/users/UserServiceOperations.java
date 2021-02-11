package com.joelkell.demo.services.users;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

public interface UserServiceOperations {
  Single<List<User>> getAllUsers();

  Maybe<User> getUserById(String id);

  Maybe<User> getUserByUsername(String id);

  TokenHttpWrapper getToken(@Body User user);

  Boolean getUserPasswordMatchesName(User user);

  UserHttpWrapper addUser(@Body User user);

  Maybe<User> deleteUser(String id);

  UserHttpWrapper updateUser(String id, @Body User user);
}
