package com.joelkell.demo.services.reviews;

import io.micronaut.http.annotation.Body;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

public interface ReviewServiceOperations {
  Single<List<Review>> getAllReviews();

  Maybe<Review> getReviewByReviewId(String id);

  Single<List<Review>> getReviewsByProductId(String id);

  Single<List<Review>> getReviewsByUserId(String id);

  ReviewHttpWrapper addReview(@Body Review review);

  Maybe<Review> deleteReview(String id);

  ReviewHttpWrapper updateReview(String id, @Body Review review);
}
