package com.example.movies.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseReviews {
    @SerializedName("docs")
    List<Review> reviews;

    public ResponseReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    @Override
    public String toString() {
        return "ResponseReviews{" +
                "reviews=" + reviews +
                '}';
    }
}
