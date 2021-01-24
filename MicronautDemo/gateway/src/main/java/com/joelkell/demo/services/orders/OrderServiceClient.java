package com.joelkell.demo.services.orders;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Client(id = "orders")
public interface OrderServiceClient extends OrderServiceOperations {
  @Get("/user/{id}")
  Single<List<Order>> getOrdersByUserId(String id);

  @Get("/order/{id}")
  Maybe<Order> getOrderByOrderId(String id);

  @Post("/")
  @Consumes(MediaType.APPLICATION_JSON)
  OrderHttpWrapper createOrder(@Body Order order);

  @Delete("/{id}")
  Maybe<Order> deleteOrderById(String id);

  @Put("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  OrderHttpWrapper updateOrder(String id, @Body Order order);
}
