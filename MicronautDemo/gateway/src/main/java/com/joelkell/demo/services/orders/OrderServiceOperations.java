package com.joelkell.demo.services.orders;

import io.micronaut.http.annotation.Body;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

public interface OrderServiceOperations {
  Single<List<Order>> getOrdersByUserId(String id);

  Maybe<Order> getOrderByOrderId(String id);

  OrderHttpWrapper createOrder(@Body Order order);

  Maybe<Order> deleteOrderById(String id);

  OrderHttpWrapper updateOrder(String id, @Body Order order);
}
