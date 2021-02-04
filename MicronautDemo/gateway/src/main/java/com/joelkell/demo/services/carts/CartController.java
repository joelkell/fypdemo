package com.joelkell.demo.services.carts;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Maybe;

import javax.validation.Valid;

@Secured(SecurityRule.IS_ANONYMOUS)
@ExecuteOn(TaskExecutors.IO)
@Controller("/carts")
public class CartController {
  private final CartServiceOperations cartServiceOperations;

  public CartController(CartServiceOperations cartServiceOperations) {
    this.cartServiceOperations = cartServiceOperations;
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Get("/{id}")
  public Maybe<Cart> getCartByUserId(String id) {
    return cartServiceOperations.getCartByUserId(id);
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Post("/createCart/")
  public HttpResponse<?> createCart(@Body @Valid Cart cart) {
    CartHttpWrapper cartHttpWrapper = cartServiceOperations.createCart(cart);
    if (cartHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getCart());
    } else {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getBody());
    }
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Post("/AddProduct/{id}")
  public HttpResponse<?> addNewProductToCart(String id, @Body @Valid CartItem cartItem) {
    CartHttpWrapper cartHttpWrapper = cartServiceOperations.addNewProductToCart(id, cartItem);
    if (cartHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getCart());
    } else {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getBody());
    }
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Delete("/{id}")
  public HttpResponse<?> deleteProductFromCart(String id, @Body @Valid CartItem cartItem) {
    CartHttpWrapper cartHttpWrapper = cartServiceOperations.deleteProductFromCart(id, cartItem);
    if (cartHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getCart());
    } else {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getBody());
    }
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Put("/{id}")
  public HttpResponse<?> updateCart(String id, @Body @Valid Cart cart) {
    CartHttpWrapper cartHttpWrapper = cartServiceOperations.updateCart(id, cart);
    if (cartHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getCart());
    } else {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getBody());
    }
  }
}
