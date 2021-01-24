package com.joelkell.demo.services.carts;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.reactivex.Maybe;

import javax.validation.Valid;

@Controller("/carts")
public class CartController {
  private final CartServiceOperations cartServiceOperations;

  public CartController(CartServiceOperations cartServiceOperations) {
    this.cartServiceOperations = cartServiceOperations;
  }

  @Get("/{id}")
  public Maybe<Cart> getCartByUserId(String id) {
    return cartServiceOperations.getCartByUserId(id);
  }

  @Post("/createCart/")
  public HttpResponse<?> createCart(@Body @Valid Cart cart) {
    CartHttpWrapper cartHttpWrapper = cartServiceOperations.createCart(cart);
    if (cartHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getCart());
    } else {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getBody());
    }
  }

  @Post("/AddProduct/{id}")
  public HttpResponse<?> addNewProductToCart(String id, @Body @Valid CartItem cartItem) {
    CartHttpWrapper cartHttpWrapper = cartServiceOperations.addNewProductToCart(id, cartItem);
    if (cartHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getCart());
    } else {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getBody());
    }
  }

  @Delete("/{id}")
  public HttpResponse<?> deleteProductFromCart(String id, @Body @Valid CartItem cartItem) {
    CartHttpWrapper cartHttpWrapper = cartServiceOperations.deleteProductFromCart(id, cartItem);
    if (cartHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getCart());
    } else {
      return HttpResponse.status(cartHttpWrapper.getResponse()).body(cartHttpWrapper.getBody());
    }
  }

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
