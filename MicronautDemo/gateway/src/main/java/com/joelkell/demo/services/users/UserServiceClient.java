package com.joelkell.demo.services.users;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.validation.Valid;
import java.util.List;

@Client(id = "users")
public interface UserServiceClient extends UserServiceOperations {

  @Get("/")
  Single<List<User>> getAllUsers();

  @Get("/{id}")
  Maybe<User> getUserById(String id);

  @Post("/password")
  @Consumes(MediaType.APPLICATION_JSON)
  Boolean getUserPasswordMatchesName(@Body @Valid User user);

  @Post
  @Consumes(MediaType.APPLICATION_JSON)
  UserHttpWrapper addUser(@Body @Valid User user);

  @Delete("/{id}")
  Maybe<User> deleteUser(String id);

  @Put("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  UserHttpWrapper updateUser(String id, @Body @Valid User user);
}
