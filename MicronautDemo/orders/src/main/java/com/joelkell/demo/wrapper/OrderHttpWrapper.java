package com.joelkell.demo.wrapper;

import com.joelkell.demo.orders.Order;
import com.joelkell.demo.orders.Status;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.reactivex.Maybe;
import io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderHttpWrapper {
    private Order order;
    private HttpStatus response;
    private String body;

    public OrderHttpWrapper(HttpResponse httpResponse) {
        if (httpResponse.getBody().get() instanceof String) {
            this.body = (String) httpResponse.getBody().get();
            this.order = new Order(new ObjectId("507f1f77bcf86cd799439011"), Status.ORDERED, BigDecimal.valueOf(1), new ArrayList<>());
        } else if (httpResponse.getBody().get() instanceof MaybeSwitchIfEmptySingle) {
            MaybeSwitchIfEmptySingle maybeSwitchIfEmptySingle =
                    (MaybeSwitchIfEmptySingle) httpResponse.getBody().get();
            Object object = maybeSwitchIfEmptySingle.blockingGet();
            this.order = (Order) object;
            this.body = "null";
        } else if (httpResponse.getBody().get() instanceof Maybe) {
            Maybe maybe = (Maybe) httpResponse.getBody().get();
            Object object = maybe.blockingGet();
            this.order = (Order) object;
            this.body = "null";
        }
        this.response = httpResponse.getStatus();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public HttpStatus getResponse() {
        return response;
    }

    public void setResponse(HttpStatus response) {
        this.response = response;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
