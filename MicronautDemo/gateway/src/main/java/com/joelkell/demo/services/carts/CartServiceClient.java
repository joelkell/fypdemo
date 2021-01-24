package com.joelkell.demo.services.carts;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;

@Client(id = "carts")
public interface CartServiceClient extends CartServiceOperations {

  @Get("/{id}")
  Maybe<Cart> getCartByUserId(String id);

  @Post("/createCart/")
  @Consumes(MediaType.APPLICATION_JSON)
  CartHttpWrapper createCart(@Body Cart cart);

  @Post("/AddProduct/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  CartHttpWrapper addNewProductToCart(String id, @Body CartItem cartItem);

  @Delete("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  CartHttpWrapper deleteProductFromCart(String id, @Body CartItem cartItem);

  @Put("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  CartHttpWrapper updateCart(String id, @Body Cart cart);
}
