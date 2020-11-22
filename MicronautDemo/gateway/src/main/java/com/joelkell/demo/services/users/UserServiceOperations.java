package com.joelkell.demo.services.users;

import io.micronaut.http.annotation.Body;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.validation.Valid;
import java.util.List;

public interface UserServiceOperations {
  Single<List<User>> getAllUsers();

  Maybe<User> getUserById(String id);

  Boolean getUserPasswordMatchesName(User user);

  UserHttpWrapper addUser(@Body @Valid User user);

  Maybe<User> deleteUser(String id);

  UserHttpWrapper updateUser(String id, @Body @Valid User user);
}
