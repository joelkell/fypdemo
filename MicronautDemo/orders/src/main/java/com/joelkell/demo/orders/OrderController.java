package com.joelkell.demo.orders;

import com.joelkell.demo.wrapper.OrderHttpWrapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller()
public class OrderController {

  @Inject private OrderDAO orderDAO;

  @Get("/user/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Single<List<Order>> getOrdersByUserId(String id) {
    return orderDAO.getOrdersByUserId(id);
  }

  @Get("/order/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Maybe<Order> getOrderByOrderId(String id) {
    return orderDAO.getOrderByOrderId(id);
  }

  @Post("/")
  @Consumes(MediaType.APPLICATION_JSON)
  public OrderHttpWrapper createOrder(@Body @Valid Order order) {
    order.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    return orderDAO.createOrder(order);
  }

  @Delete("/{id}")
  public Maybe<Order> deleteOrderById(String id) {
    return orderDAO.deleteOrderById(id);
  }

  @Put("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public OrderHttpWrapper updateOrder(String id, @Body @Valid Order order) {
    return orderDAO.updateOrder(id, order);
  }
}
