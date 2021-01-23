package com.joelkell.demo.services.reviews;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.validation.Valid;
import java.util.List;

@Client(id = "reviews")
public interface ReviewServiceClient extends ReviewServiceOperations {

    @Get()
    public Single<List<Review>> getAllReviews();

    @Get("/{id}")
    public Maybe<Review> getReviewByReviewId(String id);

    @Get("/products/{id}")
    public Single<List<Review>> getReviewsByProductId(String id);

    @Get("/users/{id}")
    public Single<List<Review>> getReviewsByUserId(String id);

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    public ReviewHttpWrapper addReview(@Body @Valid Review review);

    @Delete("/{id}")
    public Maybe<Review> deleteReview(String id);

    @Put("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ReviewHttpWrapper updateReview(String id, @Body @Valid Review review);
}
