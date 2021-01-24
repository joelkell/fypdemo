package com.joelkell.demo.orders;

import com.joelkell.demo.wrapper.OrderHttpWrapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.Filters;
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

public class OrderDAO {

  @Inject private MongoClient mongoClient;
  @Inject private OrderConfiguration config;

  @Bean
  private MongoCollection getCollection() {
    CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
    ClassModel<Order> orderModel =
        ClassModel.builder(Order.class).enableDiscriminator(true).build();
    ClassModel<OrderItem> orderItemModel =
        ClassModel.builder(OrderItem.class).enableDiscriminator(true).build();
    PojoCodecProvider pojoCodecProvider =
        PojoCodecProvider.builder().register(orderModel, orderItemModel).build();
    CodecRegistry fromProvider = CodecRegistries.fromProviders(pojoCodecProvider);
    CodecRegistry pojoCodecRegistry =
        CodecRegistries.fromRegistries(defaultCodecRegistry, fromProvider);
    return mongoClient
        .getDatabase(config.getDatabaseName())
        .withCodecRegistry(pojoCodecRegistry)
        .getCollection(config.getOrderCollectionName(), Order.class);
  }

  public Single<List<Order>> getOrdersByUserId(String id) {
    return Flowable.fromPublisher(getCollection().find(eq("userId", new ObjectId(id)))).toList();
  }

  public Maybe<Order> getOrderByOrderId(String id) {
    return Flowable.fromPublisher(getCollection().find(eq("_id", new ObjectId(id))).limit(1))
        .firstElement();
  }

  public Maybe<Order> getOrderByUserIdAndTimestamp(String id, String timestamp) {
    return Flowable.fromPublisher(
            getCollection()
                .find(Filters.and(eq("userId", new ObjectId(id)), eq("timestamp", timestamp)))
                .limit(1))
        .firstElement();
  }

  public OrderHttpWrapper createOrder(Order order) {
    Maybe<Order> orderExists =
        getOrderByUserIdAndTimestamp(order.getUserId().toString(), order.getTimestamp());
    if (orderExists.blockingGet() == null) {
      return new OrderHttpWrapper(
          HttpResponse.ok(
              Flowable.fromPublisher(getCollection().find(eq("_id", order.getId())).limit(1))
                  .firstElement()
                  .switchIfEmpty(
                      Single.fromPublisher(getCollection().insertOne(order))
                          .map(success -> order))));
    } else {
      return new OrderHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN)
              .body("An Order with this user and timestamp already exists"));
    }
  }

  public Maybe<Order> deleteOrderById(String id) {
    return Flowable.fromPublisher(getCollection().findOneAndDelete(eq("_id", new ObjectId(id))))
        .firstElement();
  }

  public OrderHttpWrapper updateOrder(String id, Order order) {
    Maybe<Order> orderExists = getOrderByOrderId(id);
    if (orderExists.blockingGet() != null) {
      if (orderExists.blockingGet().getId().toString().equals(order.getId().toString())) {
        if (orderExists.blockingGet().getUserId().toString().equals(order.getUserId().toString())) {
          if (orderExists.blockingGet().getTimestamp().equals(order.getTimestamp())) {
            return new OrderHttpWrapper(
                HttpResponse.ok(
                    Flowable.fromPublisher(
                            getCollection()
                                .findOneAndReplace(
                                    eq("_id", new ObjectId(id)),
                                    order,
                                    new FindOneAndReplaceOptions().returnDocument(AFTER)))
                        .firstElement()));
          } else {
            return new OrderHttpWrapper(
                HttpResponse.status(HttpStatus.FORBIDDEN).body("Timestamp cannot be changed"));
          }
        } else {
          return new OrderHttpWrapper(
              HttpResponse.status(HttpStatus.FORBIDDEN).body("User ID cannot be changed"));
        }
      } else {
        return new OrderHttpWrapper(
            HttpResponse.status(HttpStatus.FORBIDDEN).body("ID cannot be changed"));
      }
    } else {
      return new OrderHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN).body("No Order with this ID exists"));
    }
  }
}
