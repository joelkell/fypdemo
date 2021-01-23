package com.joelkell.demo.services.users;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Controller("/users")
public class UserController {

  private final UserServiceOperations userServiceOperations;

  public UserController(UserServiceOperations userServiceOperations) {
    this.userServiceOperations = userServiceOperations;
  }

  @Get()
  public Single<List<User>> getAllUsers() {
    return userServiceOperations.getAllUsers();
  }

  @Get("/{id}")
  public Maybe<User> getUserById(String id) {
    return userServiceOperations.getUserById(id);
  }

  @Post("/password")
  public Boolean getUserPasswordMatchesName(User user) {
    return userServiceOperations.getUserPasswordMatchesName(user);
  }

  @Post
  public HttpResponse<?> addUser(User user) {
    UserHttpWrapper userHttpWrapper = userServiceOperations.addUser(user);
    if (userHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(userHttpWrapper.getResponse()).body(userHttpWrapper.getUser());
    } else {
      return HttpResponse.status(userHttpWrapper.getResponse()).body(userHttpWrapper.getBody());
    }
  }

  @Delete("/{id}")
  public Maybe<User> deleteUser(String id) {
    return userServiceOperations.deleteUser(id);
  }

  @Put("/{id}")
  public HttpResponse<?> updateUser(String id, User user) {
    UserHttpWrapper userHttpWrapper = userServiceOperations.updateUser(id, user);
    if (userHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(userHttpWrapper.getResponse()).body(userHttpWrapper.getUser());
    } else {
      return HttpResponse.status(userHttpWrapper.getResponse()).body(userHttpWrapper.getBody());
    }
  }
}
