package com.joelkell.demo.services.products;

import io.micronaut.http.annotation.Body;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

public interface ProductServiceOperations {
  Single<List<Product>> getAllProducts();

  Maybe<Product> getProductById(String id);

  Single<List<Product>> getProductsByCategory(String category);

  ProductHttpWrapper addProduct(@Body Product product);

  Maybe<Product> deleteProduct(String id);

  ProductHttpWrapper updateProduct(String id, @Body Product product);
}
