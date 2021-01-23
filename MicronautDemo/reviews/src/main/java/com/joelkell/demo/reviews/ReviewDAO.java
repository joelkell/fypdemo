package com.joelkell.demo.reviews;

import com.joelkell.demo.wrapper.ReviewHttpWrapper;
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
public class ReviewDAO {

  @Inject private MongoClient mongoClient;
  @Inject private ReviewConfiguration config;

  @Bean
  private MongoCollection getCollection() {
    CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
    ClassModel<Review> reviewModel =
        ClassModel.builder(Review.class).enableDiscriminator(true).build();
    PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(reviewModel).build();
    CodecRegistry fromProvider = CodecRegistries.fromProviders(pojoCodecProvider);
    CodecRegistry pojoCodecRegistry =
        CodecRegistries.fromRegistries(defaultCodecRegistry, fromProvider);
    return mongoClient
        .getDatabase(config.getDatabaseName())
        .withCodecRegistry(pojoCodecRegistry)
        .getCollection(config.getCollectionName(), Review.class);
  }

  public Maybe<Review> getReviewByReviewId(String id) {
    return Flowable.fromPublisher(getCollection().find(eq("_id", new ObjectId(id))).limit(1))
        .firstElement();
  }

  public Single<List<Review>> getReviewsByProductId(String productId) {
    return Flowable.fromPublisher(getCollection().find(eq("productId", new ObjectId(productId))))
        .toList();
  }

  public Single<List<Review>> getReviewsByUserId(String userId) {
    return Flowable.fromPublisher(getCollection().find(eq("userId", new ObjectId(userId))))
        .toList();
  }

  public Maybe<Review> getProductIdAndUserIdExists(ObjectId productId, ObjectId userId) {
    Maybe<Review> existingReview =
        Flowable.fromPublisher(
                getCollection()
                    .find(and(eq("productId", productId), eq("userId", userId)))
                    .limit(1))
            .firstElement();
    return existingReview;
  }

  public ReviewHttpWrapper addReview(Review review) {
    Maybe<Review> productIDAndUserIDExists =
        getProductIdAndUserIdExists(review.getProductId(), review.getUserId());
    if (productIDAndUserIDExists.blockingGet() == null) {
      return new ReviewHttpWrapper(
          HttpResponse.ok(
              Flowable.fromPublisher(getCollection().find(eq("_id", review.getId())).limit(1))
                  .firstElement()
                  .switchIfEmpty(
                      Single.fromPublisher(getCollection().insertOne(review))
                          .map(success -> review))));
    }
    return new ReviewHttpWrapper(
        HttpResponse.status(HttpStatus.FORBIDDEN)
            .body("A review with this user already exists for this product"));
  }

  public Maybe<Review> deleteReview(String id) {
    return Flowable.fromPublisher(getCollection().findOneAndDelete(eq("_id", new ObjectId(id))))
        .firstElement();
  }



  public ReviewHttpWrapper updateReview(String id, Review review) {
    Maybe<Review> reviewExists = getReviewByReviewId(id);

    if (reviewExists.blockingGet().getUserId().toString().equals(review.getUserId().toString())) {
      if (reviewExists
          .blockingGet()
          .getProductId()
          .toString()
          .equals(review.getProductId().toString())) {
        return new ReviewHttpWrapper(
            HttpResponse.ok(
                Flowable.fromPublisher(
                        getCollection()
                            .findOneAndReplace(
                                eq("_id", new ObjectId(id)),
                                review,
                                new FindOneAndReplaceOptions().returnDocument(AFTER)))
                    .firstElement()));
      }
      return new ReviewHttpWrapper(
          HttpResponse.status(HttpStatus.FORBIDDEN)
              .body("Cannot update Product ID for this review"));
    }
    return new ReviewHttpWrapper(
        HttpResponse.status(HttpStatus.FORBIDDEN).body("Cannot update User ID for this review"));
  }

  public Single<List<Review>> getAllReviews() {
    return Flowable.fromPublisher(getCollection().find()).toList();
  }
}
