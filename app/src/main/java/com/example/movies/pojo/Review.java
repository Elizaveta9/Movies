package com.example.movies.pojo;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("type")
    private String type;
    @SerializedName("review")
    private String review;
    @SerializedName("author")
    private String author;

    public Review(String type, String review, String author) {
        this.type = type;
        this.review = review;
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public String getReview() {
        return review;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Review{" +
                "type='" + type + '\'' +
                ", review='" + review + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
