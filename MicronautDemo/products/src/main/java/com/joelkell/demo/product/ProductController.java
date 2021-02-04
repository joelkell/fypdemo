package com.joelkell.demo.product;

import com.joelkell.demo.wrapper.ProductHttpWrapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Controller()
public class ProductController {

  @Inject private ProductDAO productDAO;

  @Get()
  @Produces(MediaType.APPLICATION_JSON)
  public Single<List<Product>> getAllProducts() {
    return productDAO.getAllProducts();
  }

  @Get("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Maybe<Product> getProductById(String id) {
    return productDAO.getProductById(id);
  }

  @Get("/category/{category}")
  @Produces(MediaType.APPLICATION_JSON)
  public Single<List<Product>> getProductsByCategory(String category) {
    return productDAO.getProductsByCategory(category);
  }

  @Post
  @Consumes(MediaType.APPLICATION_JSON)
  public ProductHttpWrapper addProduct(@Body @Valid Product product) {
    return productDAO.addProduct(product);
  }

  @Delete("/{id}")
  public Maybe<Product> deleteProduct(String id) {
    return productDAO.deleteProduct(id);
  }

  @Put("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public ProductHttpWrapper updateProduct(String id, @Body @Valid Product product) {
    return productDAO.updateProduct(id, product);
  }
}
