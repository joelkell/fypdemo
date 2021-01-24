package com.joelkell.demo.carts;

import com.joelkell.demo.wrapper.CartHttpWrapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.reactivex.Maybe;

import javax.inject.Inject;
import javax.validation.Valid;
import java.math.BigDecimal;

@Controller()
public class CartController {

  @Inject private CartDAO cartDAO;

  @Get("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Maybe<Cart> getCartByUserId(String id) {
    return cartDAO.getCartByUserId(id);
  }

  @Post("/createCart/")
  @Consumes(MediaType.APPLICATION_JSON)
  public CartHttpWrapper createCart(@Body @Valid Cart cart) {
    cart.setDeliveryPrice(new BigDecimal(0));
    cart.setTotalPrice(new BigDecimal(0));
    return cartDAO.createCart(cart);
  }

  @Post("/AddProduct/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public CartHttpWrapper addNewProductToCart(String id, @Body @Valid CartItem cartItem) {
    return cartDAO.addNewProductToCart(id, cartItem);
  }

  @Delete("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public CartHttpWrapper deleteProductFromCart(String id, @Body @Valid CartItem cartItem) {
    return cartDAO.deleteProductFromCart(id, cartItem);
  }

  @Put("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public CartHttpWrapper updateCart(String id, @Body @Valid Cart cart) {
    return cartDAO.updateCart(id, cart);
  }
}
