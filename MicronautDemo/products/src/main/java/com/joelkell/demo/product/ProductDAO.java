package com.joelkell.demo.product;

import com.joelkell.demo.wrapper.ProductHttpWrapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.context.annotation.Bean;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.ReturnDocument.AFTER;

@Singleton
public class ProductDAO {

  @Inject private MongoClient mongoClient;
  @Inject private ProductConfiguration config;

  @Bean
  private MongoCollection getCollection() {
    CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
    ClassModel<Product> productModel =
        ClassModel.builder(Product.class).enableDiscriminator(true).build();
    PojoCodecProvider pojoCodecProvider =
        PojoCodecProvider.builder().register(productModel).build();
    CodecRegistry fromProvider = CodecRegistries.fromProviders(pojoCodecProvider);
    CodecRegistry pojoCodecRegistry =
        CodecRegistries.fromRegistries(defaultCodecRegistry, fromProvider);
    return mongoClient
        .getDatabase(config.getDatabaseName())
        .withCodecRegistry(pojoCodecRegistry)
        .getCollection(config.getCollectionName(), Product.class);
  }

  public Single<List<Product>> getAllProducts() {
    return Flowable.fromPublisher(getCollection().find()).toList();
  }

  public Maybe<Product> getProductById(String id) {
    return Flowable.fromPublisher(getCollection().find(eq("_id", new ObjectId(id))).limit(1))
        .firstElement();
  }

  public Maybe<Product> getProductByNameAndDescription(String name, String description) {
    return Flowable.fromPublisher(
            getCollection().find(and(eq("name", name), eq("description", description))).limit(1))
        .firstElement();
  }

  public Single<List<Product>> getProductsByCategory(String category) {
    return Flowable.fromPublisher(getCollection().find(eq("categories", category))).toList();
  }

  public ProductHttpWrapper addProduct(Product product) {
    Maybe<Product> productNameAndDescriptionExists =
        getProductByNameAndDescription(product.getName(), product.getDescription());
    if (productNameAndDescriptionExists.blockingGet() == null) {
      return new ProductHttpWrapper(
          HttpResponse.ok(
              Flowable.fromPublisher(getCollection().find(eq("_id", product.getId())).limit(1))
                  .firstElement()
                  .switchIfEmpty(
                      Single.fromPublisher(getCollection().insertOne(product))
                          .map(success -> product))));
    }
    return new ProductHttpWrapper(
        HttpResponse.status(HttpStatus.FORBIDDEN)
            .body("A Product with this name and description already exists"));
  }

  public Maybe<Product> deleteProduct(String id) {
    return Flowable.fromPublisher(getCollection().findOneAndDelete(eq("_id", new ObjectId(id))))
        .firstElement();
  }

  public ProductHttpWrapper updateProduct(String id, Product product) {
    Maybe<Product> productNameAndDescriptionExists =
        getProductByNameAndDescription(product.getName(), product.getDescription());
    if ((productNameAndDescriptionExists.blockingGet() != null
            && productNameAndDescriptionExists.blockingGet().getId().toString().equals(id))
        || productNameAndDescriptionExists.blockingGet() == null) {

      return new ProductHttpWrapper(
          HttpResponse.ok(
              Flowable.fromPublisher(
                      getCollection()
                          .findOneAndReplace(
                              eq("_id", new ObjectId(id)),
                              product,
                              new FindOneAndReplaceOptions().returnDocument(AFTER)))
                  .firstElement()));
    }
    return new ProductHttpWrapper(
        HttpResponse.status(HttpStatus.FORBIDDEN)
            .body("A Product with this name and description already exists"));
  }
}
