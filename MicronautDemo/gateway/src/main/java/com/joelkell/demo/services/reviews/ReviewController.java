package com.joelkell.demo.services.reviews;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Controller("/reviews")
public class ReviewController {
  private final ReviewServiceOperations reviewServiceOperations;

  public ReviewController(ReviewServiceOperations reviewServiceOperations) {
    this.reviewServiceOperations = reviewServiceOperations;
  }

  @Get()
  public Single<List<Review>> getAllReviews() {
    return reviewServiceOperations.getAllReviews();
  }

  @Get("/{id}")
  public Maybe<Review> getReviewByReviewId(String id) {
    return reviewServiceOperations.getReviewByReviewId(id);
  }

  @Get("/products/{id}")
  public Single<List<Review>> getReviewsByProductId(String id) {
    return reviewServiceOperations.getReviewsByProductId(id);
  }

  @Get("/users/{id}")
  public Single<List<Review>> getReviewsByUserId(String id) {
    return reviewServiceOperations.getReviewsByUserId(id);
  }

  @Post
  public HttpResponse<?> addReview(Review review) {
    ReviewHttpWrapper reviewHttpWrapper = reviewServiceOperations.addReview(review);
    if (reviewHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(reviewHttpWrapper.getResponse())
          .body(reviewHttpWrapper.getReview());
    } else {
      return HttpResponse.status(reviewHttpWrapper.getResponse()).body(reviewHttpWrapper.getBody());
    }
  }

  @Delete("/{id}")
  public Maybe<Review> deleteReview(String id) {
    return reviewServiceOperations.deleteReview(id);
  }

  @Put("/{id}")
  public HttpResponse<?> updateReview(String id, Review review) {
    ReviewHttpWrapper reviewHttpWrapper = reviewServiceOperations.updateReview(id, review);
    if (reviewHttpWrapper.getBody().equals("null")) {
      return HttpResponse.status(reviewHttpWrapper.getResponse())
          .body(reviewHttpWrapper.getReview());
    } else {
      return HttpResponse.status(reviewHttpWrapper.getResponse()).body(reviewHttpWrapper.getBody());
    }
  }
}
