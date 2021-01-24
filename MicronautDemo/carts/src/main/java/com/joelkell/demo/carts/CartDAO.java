package com.joelkell.demo.carts;

import com.joelkell.demo.wrapper.CartHttpWrapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.context.annotation.Bean;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.ReturnDocument.AFTER;

public class CartDAO {

  @Inject private MongoClient mongoClient;
  @Inject private CartConfiguration config;

  @Client(id = "gateway")
  @Inject
  RxHttpClient httpClient;

  private static final Logger LOGGER = LoggerFactory.getLogger(CartDAO.class);

  @Bean
  private MongoCollection getCollection() {
    CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
    ClassModel<Cart> cartModel = ClassModel.builder(Cart.class).enableDiscriminator(true).build();
    ClassModel<CartItem> cartItemModel =
        ClassModel.builder(CartItem.class).enableDiscriminator(true).build();
    PojoCodecProvider pojoCodecProvider =
        PojoCodecProvider.builder().register(cartModel, cartItemModel).build();
    CodecRegistry fromProvider = CodecRegistries.fromProviders(pojoCodecProvider);
    CodecRegistry pojoCodecRegistry =
        CodecRegistries.fromRegistries(defaultCodecRegistry, fromProvider);
    return mongoClient
        .getDatabase(config.getDatabaseName())
        .withCodecRegistry(pojoCodecRegistry)
        .getCollection(config.getCartCollectionName(), Cart.class);
  }

  public Maybe<Cart> getCartByUserId(String id) {
    return Flowable.fromPublisher(getCollection().find(eq("userId", new ObjectId(id))).limit(1))
        .firstElement();
  }

  public Maybe<Cart> getCartByCartId(String id) {
    return Flowable.fromPublisher(getCollection().find(eq("_id", new ObjectId(id))).limit(1))
        .firstElement();
  }

  public boolean checkIfUserIsReal(Cart cart) {
    try {
      httpClient.toBlocking().exchange("/users/" + cart.getUserId());
      return true;
    } catch (Exception ex) {
      LOGGER.error("Cart DAO: ", ex);
    }
    return false;
  }

  public CartHttpWrapper createCart(Cart cart) {
    if (checkIfUserIsReal(cart)) {
      Cart cartExists = getCartByUserId(cart.getUserId().toString()).blockingGet();
      if (cartExists == null) {
        return new CartHttpWrapper(
            HttpResponse.ok(
                Flowable.fromPublisher(getCollection().find(eq("_id", cart.getId())).limit(1))
                    .firstElement()
                    .switchIfEmpty(
                        Single.fromPublisher(getCollection().insertOne(cart))
                            .map(success -> cart))));
      } else {
        return new CartHttpWrapper(
            HttpResponse.status(HttpStatus.FORBIDDEN).body("A Cart already exists for this user"));
      }
    } else {
      return new CartHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN).body("No user with that ID exists"));
    }
  }

  public boolean cartHasProduct(Cart cart, String productId) {
    for (int i = 0; i < cart.getCartItems().size(); i++) {
      if (cart.getCartItems().get(i).getProductId().toString().equals(productId)) {
        return true;
      }
    }
    return false;
  }

  public int productPositionInCart(Cart cart, String productId) {
    int position = -1;
    for (int i = 0; i < cart.getCartItems().size(); i++) {
      if (cart.getCartItems().get(i).getProductId().toString().equals(productId)) {
        position = i;
      }
    }
    return position;
  }

  public boolean checkIfProductIsReal(CartItem cartItem) {
    try {
      httpClient.toBlocking().exchange("/products/" + cartItem.getProductId());
      return true;
    } catch (Exception ex) {
      LOGGER.error("Cart DAO: ", ex);
    }
    return false;
  }

  public CartHttpWrapper addNewProductToCart(String id, CartItem cartItem) {
    if (checkIfProductIsReal(cartItem)) {
      String productId = cartItem.getProductId().toString();
      Cart cartExists = getCartByUserId(id).blockingGet();
      if (cartExists != null) {
        if (cartHasProduct(cartExists, productId)) {
          int position = productPositionInCart(cartExists, productId);
          int quantity =
              cartExists.getCartItems().get(position).getQuantity() + cartItem.getQuantity();
          cartExists.getCartItems().get(position).setQuantity(quantity);
        } else {
          cartExists.getCartItems().add(cartItem);
        }
        return new CartHttpWrapper(
            HttpResponse.ok(
                Flowable.fromPublisher(
                        getCollection()
                            .findOneAndReplace(
                                eq("userId", new ObjectId(id)),
                                cartExists,
                                new FindOneAndReplaceOptions().returnDocument(AFTER)))
                    .firstElement()));
      } else {
        return new CartHttpWrapper(
            HttpResponse.status(HttpStatus.FORBIDDEN).body("No cart with that User ID exists"));
      }
    } else {
      return new CartHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN).body("No product with that ID exists"));
    }
  }

  public CartHttpWrapper deleteProductFromCart(String id, CartItem cartItem) {
    String productId = cartItem.getProductId().toString();
    Cart cartExists = getCartByUserId(id).blockingGet();
    if (cartExists != null) {
      if (cartHasProduct(cartExists, productId)) {
        int position = productPositionInCart(cartExists, productId);
        List<CartItem> cartItemsList = cartExists.getCartItems();
        cartItemsList.remove(position);
        cartExists.setCartItems(cartItemsList);
        return new CartHttpWrapper(
            HttpResponse.ok(
                Flowable.fromPublisher(
                        getCollection()
                            .findOneAndReplace(
                                eq("userId", new ObjectId(id)),
                                cartExists,
                                new FindOneAndReplaceOptions().returnDocument(AFTER)))
                    .firstElement()));
      } else {
        return new CartHttpWrapper(
            HttpResponse.status(HttpStatus.FORBIDDEN).body("Cart does not contain this product"));
      }
    } else {
      return new CartHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN).body("No cart with that User ID exists"));
    }
  }

  public CartHttpWrapper updateCart(String id, Cart cart) {
    Cart cartExists = getCartByUserId(id).blockingGet();
    if (cartExists == null) {
      return new CartHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN).body("No Cart with this User Id Exists"));
    }
    if (!cartExists.getUserId().toString().equals(cart.getUserId().toString())) {
      return new CartHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN).body("User ID cannot be changed"));
    }
    if (!cartExists.getId().toString().equals(cart.getId().toString())) {
      return new CartHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN).body("Cart ID cannot be changed"));
    }

    return new CartHttpWrapper(
        HttpResponse.ok(
            Flowable.fromPublisher(
                    getCollection()
                        .findOneAndReplace(
                            eq("userId", new ObjectId(id)),
                            cart,
                            new FindOneAndReplaceOptions().returnDocument(AFTER)))
                .firstElement()));
  }
}
