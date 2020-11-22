package com.joelkell.demo.services.products;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Client(id = "products")
public interface ProductServiceClient extends ProductServiceOperations {

  @Get("/")
  Single<List<Product>> getAllProducts();

  @Get("/{id}")
  Maybe<Product> getProductById(String id);

  @Get("/category/{category}")
  Single<List<Product>> getProductsByCategory(String category);

  @Post
  @Consumes(MediaType.APPLICATION_JSON)
  ProductHttpWrapper addProduct(@Body Product product);

  @Delete("/{id}")
  Maybe<Product> deleteProduct(String id);

  @Put("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  ProductHttpWrapper updateProduct(String id, @Body Product product);
}
