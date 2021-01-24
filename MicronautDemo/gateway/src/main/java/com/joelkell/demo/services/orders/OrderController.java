package com.joelkell.demo.services.orders;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.validation.Valid;
import java.util.List;

@Controller("/orders")
public class OrderController {

  private final OrderServiceOperations orderServiceOperations;

  public OrderController(OrderServiceOperations orderServiceOperations) {
    this.orderServiceOperations = orderServiceOperations;
  }

  @Get("/user/{id}")
  public Single<List<Order>> getOrdersByUserId(String id) {
    return orderServiceOperations.getOrdersByUserId(id);
  }

  @Get("/order/{id}")
  public Maybe<Order> getOrderByOrderId(String id) {
    return orderServiceOperations.getOrderByOrderId(id);
  }

  @Post("/")
  public HttpResponse<?> createOrder(@Body @Valid Order order) {
    OrderHttpWrapper orderHttpWrapper = orderServiceOperations.createOrder(order);
    if (orderHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(orderHttpWrapper.getResponse()).body(orderHttpWrapper.getOrder());
    } else {
      return HttpResponse.status(orderHttpWrapper.getResponse()).body(orderHttpWrapper.getBody());
    }
  }

  @Delete("/{id}")
  public Maybe<Order> deleteOrderById(String id) {
    return orderServiceOperations.deleteOrderById(id);
  }

  @Put("/{id}")
  public HttpResponse<?> updateOrder(String id, @Body @Valid Order order) {
    OrderHttpWrapper orderHttpWrapper = orderServiceOperations.updateOrder(id, order);
    if (orderHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(orderHttpWrapper.getResponse()).body(orderHttpWrapper.getOrder());
    } else {
      return HttpResponse.status(orderHttpWrapper.getResponse()).body(orderHttpWrapper.getBody());
    }
  }
}
