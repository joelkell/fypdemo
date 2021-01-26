package com.joelkell.demo.services.reviews;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/reviews")
public class ReviewController {
  private final ReviewServiceOperations reviewServiceOperations;

  public ReviewController(ReviewServiceOperations reviewServiceOperations) {
    this.reviewServiceOperations = reviewServiceOperations;
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Get()
  public Single<List<Review>> getAllReviews() {
    return reviewServiceOperations.getAllReviews();
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Get("/{id}")
  public Maybe<Review> getReviewByReviewId(String id) {
    return reviewServiceOperations.getReviewByReviewId(id);
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Get("/products/{id}")
  public Single<List<Review>> getReviewsByProductId(String id) {
    return reviewServiceOperations.getReviewsByProductId(id);
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Get("/users/{id}")
  public Single<List<Review>> getReviewsByUserId(String id) {
    return reviewServiceOperations.getReviewsByUserId(id);
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
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

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Delete("/{id}")
  public Maybe<Review> deleteReview(String id) {
    return reviewServiceOperations.deleteReview(id);
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
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
