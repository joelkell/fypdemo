package com.joelkell.demo.wrapper;

import com.joelkell.demo.reviews.Review;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.reactivex.Maybe;
import io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle;
import org.bson.types.ObjectId;

public class ReviewHttpWrapper {
    private Review review;
    private HttpStatus response;
    private String body;

    public ReviewHttpWrapper(HttpResponse httpResponse) {
        if (httpResponse.getBody().get() instanceof String) {
            this.body = (String) httpResponse.getBody().get();
            this.review = new Review(new ObjectId("507f1f77bcf86cd799439011"),new ObjectId("507f1f77bcf86cd799439011"), 0, "null");
        } else if (httpResponse.getBody().get() instanceof MaybeSwitchIfEmptySingle) {
            MaybeSwitchIfEmptySingle maybeSwitchIfEmptySingle =
                    (MaybeSwitchIfEmptySingle) httpResponse.getBody().get();
            Object object = maybeSwitchIfEmptySingle.blockingGet();
            this.review = (Review) object;
            this.body = "null";
        } else if (httpResponse.getBody().get() instanceof Maybe) {
            Maybe maybe = (Maybe) httpResponse.getBody().get();
            Object object = maybe.blockingGet();
            this.review = (Review) object;
            this.body = "null";
        }
        this.response = httpResponse.getStatus();
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public HttpStatus getResponse() {
        return response;
    }

    public void setResponse(HttpStatus response) {
        this.response = response;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
