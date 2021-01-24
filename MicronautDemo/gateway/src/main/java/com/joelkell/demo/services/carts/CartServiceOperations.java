package com.joelkell.demo.services.carts;

import io.micronaut.http.annotation.Body;
import io.reactivex.Maybe;

public interface CartServiceOperations {
  Maybe<Cart> getCartByUserId(String id);

  CartHttpWrapper createCart(@Body Cart cart);

  CartHttpWrapper addNewProductToCart(String id, @Body CartItem cartItem);

  CartHttpWrapper deleteProductFromCart(String id, @Body CartItem cartItem);

  CartHttpWrapper updateCart(String id, @Body Cart cart);
}
