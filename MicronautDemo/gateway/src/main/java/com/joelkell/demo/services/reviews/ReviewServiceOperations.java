package com.joelkell.demo.services.reviews;

import io.micronaut.http.annotation.Body;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.validation.Valid;
import java.util.List;

public interface ReviewServiceOperations {
  Single<List<Review>> getAllReviews();

  Maybe<Review> getReviewByReviewId(String id);

  Single<List<Review>> getReviewsByProductId(String id);

  Single<List<Review>> getReviewsByUserId(String id);

  ReviewHttpWrapper addReview(@Body @Valid Review review);

  Maybe<Review> deleteReview(String id);

  ReviewHttpWrapper updateReview(String id, @Body @Valid Review review);
}
