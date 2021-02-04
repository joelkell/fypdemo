package com.joelkell.demo.user;

import com.joelkell.demo.security.BCryptPasswordEncoderService;
import com.joelkell.demo.wrapper.TokenHttpWrapper;
import com.joelkell.demo.wrapper.UserHttpWrapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Controller()
public class UserController {

  private BCryptPasswordEncoderService passwordEncoder = new BCryptPasswordEncoderService();

  @Inject private UserDAO userDAO;

  @Get()
  @Produces(MediaType.APPLICATION_JSON)
  public Single<List<User>> getAllUsers() {
    return userDAO.getAllUsers();
  }

  @Get("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Maybe<User> getUserById(String id) {
    return userDAO.getUserById(id);
  }

  @Post("/login")
  @Consumes(MediaType.APPLICATION_JSON)
  public TokenHttpWrapper getToken(@Body @Valid User user) {
    return userDAO.getToken(user);
  }

  @Post("/password")
  @Consumes(MediaType.APPLICATION_JSON)
  public Boolean getUserPasswordMatchesName(@Body @Valid User user) {
    return userDAO.getUserPasswordMatchesName(user);
  }

  @Post
  @Consumes(MediaType.APPLICATION_JSON)
  public UserHttpWrapper addUser(@Body @Valid User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userDAO.addUser(user);
  }

  @Delete("/{id}")
  public Maybe<User> deleteUser(String id) {
    return userDAO.deleteUser(id);
  }

  @Put("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public UserHttpWrapper updateUser(String id, @Body @Valid User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userDAO.updateUser(id, user);
  }
}
