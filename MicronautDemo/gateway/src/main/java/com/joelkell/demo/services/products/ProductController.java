package com.joelkell.demo.services.products;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/products")
public class ProductController {

  private final ProductServiceOperations productServiceOperations;

  public ProductController(ProductServiceOperations productServiceOperations) {
    this.productServiceOperations = productServiceOperations;
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Get()
  public Single<List<Product>> getAllProducts() {
    return productServiceOperations.getAllProducts();
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Get("/{id}")
  public Maybe<Product> getProductById(String id) {
    return productServiceOperations.getProductById(id);
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Get("/category/{category}")
  public Single<List<Product>> getProductsByCategory(String category) {
    return productServiceOperations.getProductsByCategory(category);
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Post
  public HttpResponse<?> addProduct(Product product) {
    ProductHttpWrapper productHttpWrapper = productServiceOperations.addProduct(product);
    if (productHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(productHttpWrapper.getResponse())
          .body(productHttpWrapper.getProduct());
    } else {
      return HttpResponse.status(productHttpWrapper.getResponse())
          .body(productHttpWrapper.getBody());
    }
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Delete("/{id}")
  public Maybe<Product> deleteProduct(String id) {
    return productServiceOperations.deleteProduct(id);
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Put("/{id}")
  public HttpResponse<?> updateProduct(String id, Product product) {
    ProductHttpWrapper productHttpWrapper = productServiceOperations.updateProduct(id, product);
    if (productHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(productHttpWrapper.getResponse())
          .body(productHttpWrapper.getProduct());
    } else {
      return HttpResponse.status(productHttpWrapper.getResponse())
          .body(productHttpWrapper.getBody());
    }
  }
}
