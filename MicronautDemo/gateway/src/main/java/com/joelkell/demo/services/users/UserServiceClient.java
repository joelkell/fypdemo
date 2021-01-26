package com.joelkell.demo.services.users;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Client(id = "users")
public interface UserServiceClient extends UserServiceOperations {

  @Get("/")
  Single<List<User>> getAllUsers();

  @Get("/{id}")
  Maybe<User> getUserById(String id);

  @Post("/login")
  @Consumes(MediaType.APPLICATION_JSON)
  TokenHttpWrapper getToken(@Body User user);

  @Post("/password")
  @Consumes(MediaType.APPLICATION_JSON)
  Boolean getUserPasswordMatchesName(@Body User user);

  @Post
  @Consumes(MediaType.APPLICATION_JSON)
  UserHttpWrapper addUser(@Body User user);

  @Delete("/{id}")
  Maybe<User> deleteUser(String id);

  @Put("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  UserHttpWrapper updateUser(String id, @Body User user);
}
