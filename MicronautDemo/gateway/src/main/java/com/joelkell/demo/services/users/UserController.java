package com.joelkell.demo.services.users;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@ExecuteOn(TaskExecutors.IO)
@Controller("/users")
public class UserController {

  private final UserServiceOperations userServiceOperations;

  public UserController(UserServiceOperations userServiceOperations) {
    this.userServiceOperations = userServiceOperations;
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Get()
  public Single<List<User>> getAllUsers() {
    return userServiceOperations.getAllUsers();
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Get("/{id}")
  public Maybe<User> getUserById(String id) {
    return userServiceOperations.getUserById(id);
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Post("/password")
  public Boolean getUserPasswordMatchesName(User user) {
    return userServiceOperations.getUserPasswordMatchesName(user);
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Post("/login")
  @Consumes(MediaType.APPLICATION_JSON)
  public HttpResponse<?> getToken(User user) {
    TokenHttpWrapper tokenHttpWrapper = userServiceOperations.getToken(user);
    if (tokenHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(tokenHttpWrapper.getResponse())
          .body(tokenHttpWrapper.getBearerAccessRefreshToken());
    } else {
      return HttpResponse.status(tokenHttpWrapper.getResponse()).body(tokenHttpWrapper.getBody());
    }
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Post
  public HttpResponse<?> addUser(User user) {
    UserHttpWrapper userHttpWrapper = userServiceOperations.addUser(user);
    if (userHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(userHttpWrapper.getResponse()).body(userHttpWrapper.getUser());
    } else {
      return HttpResponse.status(userHttpWrapper.getResponse()).body(userHttpWrapper.getBody());
    }
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Delete("/{id}")
  public Maybe<User> deleteUser(String id) {
    return userServiceOperations.deleteUser(id);
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
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
