package com.joelkell.demo.user;

import com.joelkell.demo.security.BCryptPasswordEncoderService;
import com.joelkell.demo.wrapper.UserHttpWrapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.context.annotation.Bean;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.ReturnDocument.AFTER;

public class UserDAO {

  private BCryptPasswordEncoderService passwordEncoder = new BCryptPasswordEncoderService();

  @Inject private MongoClient mongoClient;
  @Inject private UserConfiguration config;

  @Bean
  private MongoCollection getCollection() {
    CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
    ClassModel<User> userModel = ClassModel.builder(User.class).enableDiscriminator(true).build();
    PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(userModel).build();
    CodecRegistry fromProvider = CodecRegistries.fromProviders(pojoCodecProvider);
    CodecRegistry pojoCodecRegistry =
        CodecRegistries.fromRegistries(defaultCodecRegistry, fromProvider);
    return mongoClient
        .getDatabase(config.getDatabaseName())
        .withCodecRegistry(pojoCodecRegistry)
        .getCollection(config.getCollectionName(), User.class);
  }

  public Single<List<User>> getAllUsers() {
    return Flowable.fromPublisher(getCollection().find()).toList();
  }

  public Maybe<User> getUserById(String id) {
    return Flowable.fromPublisher(getCollection().find(eq("_id", new ObjectId(id))).limit(1))
        .firstElement();
  }

  public Maybe<User> getUserByUsername(String username) {
    return Flowable.fromPublisher(getCollection().find(eq("username", username)).limit(1))
        .firstElement();
  }

  public Maybe<User> getUserByEmail(String email) {
    return Flowable.fromPublisher(getCollection().find(eq("email", email)).limit(1)).firstElement();
  }

  public Boolean getUserPasswordMatchesName(User user){
    Maybe<User> usernameExists = getUserByUsername(user.getUsername());
    if (usernameExists.blockingGet() != null) {
      User userExists = usernameExists.blockingGet();
      return passwordEncoder.matches(user.getPassword(), userExists.getPassword());
    }
    else{
      return false;
    }
  }

  public UserHttpWrapper addUser(User user) {
    Maybe<User> usernameExists = getUserByUsername(user.getUsername());
    if (usernameExists.blockingGet() == null) {
      Maybe<User> userEmailExists = getUserByEmail(user.getEmail());
      if (userEmailExists.blockingGet() == null) {
        return new UserHttpWrapper(
            HttpResponse.ok(
                Flowable.fromPublisher(getCollection().find(eq("_id", user.getId())).limit(1))
                    .firstElement()
                    .switchIfEmpty(
                        Single.fromPublisher(getCollection().insertOne(user))
                            .map(success -> user))));
      }
      return new UserHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN).body("Email already exists"));
    }
    return new UserHttpWrapper(
        HttpResponse.status(HttpStatus.FORBIDDEN).body("Username already exists"));
  }

  public Maybe<User> deleteUser(String id) {
    return Flowable.fromPublisher(getCollection().findOneAndDelete(eq("_id", new ObjectId(id))))
        .firstElement();
  }

  public UserHttpWrapper updateUser(String id, User user) {
    Maybe<User> usernameExists = getUserByUsername(user.getUsername());
    Maybe<User> userEmailExists = getUserByEmail(user.getEmail());
    if ((usernameExists.blockingGet() != null
            && usernameExists.blockingGet().getId().toString().equals(id))
        || usernameExists.blockingGet() == null) {
      if ((userEmailExists.blockingGet() != null
              && userEmailExists.blockingGet().getId().toString().equals(id))
          || userEmailExists.blockingGet() == null) {
        return new UserHttpWrapper(
            HttpResponse.ok(
                Flowable.fromPublisher(
                        getCollection()
                            .findOneAndReplace(
                                eq("_id", new ObjectId(id)),
                                user,
                                new FindOneAndReplaceOptions().returnDocument(AFTER)))
                    .firstElement()));
      }
      return new UserHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN).body("Email already exists"));
    }
    return new UserHttpWrapper(
        HttpResponse.status(HttpStatus.FORBIDDEN).body("Username already exists"));
  }
}
