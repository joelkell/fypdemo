package com.joelkell.demo.reviews;

import com.joelkell.demo.wrapper.ReviewHttpWrapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

@Controller()
public class ReviewController {

  @Inject private ReviewDAO reviewDAO;

  @Get()
  @Produces(MediaType.APPLICATION_JSON)
  public Single<List<Review>> getAllReviews() {
    return reviewDAO.getAllReviews();
  }

  @Get("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Maybe<Review> getReviewByReviewId(String id) {
    return reviewDAO.getReviewByReviewId(id);
  }

  @Get("/products/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Single<List<Review>> getReviewsByProductId(String id) {
    return reviewDAO.getReviewsByProductId(id);
  }

  @Get("/users/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Single<List<Review>> getReviewsByUserId(String id) {
    return reviewDAO.getReviewsByUserId(id);
  }

  @Post
  @Consumes(MediaType.APPLICATION_JSON)
  public ReviewHttpWrapper addReview(@Body @Valid Review review) {
    return reviewDAO.addReview(review);
  }

  @Delete("/{id}")
  public Maybe<Review> deleteReview(String id) {
    return reviewDAO.deleteReview(id);
  }

  @Put("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public ReviewHttpWrapper updateReview(String id, @Body @Valid Review review) {
    return reviewDAO.updateReview(id, review);
  }
}
