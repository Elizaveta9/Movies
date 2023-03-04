package com.example.movies.pojo;

import com.google.gson.annotations.SerializedName;

public class ResponceMovieDetail {
    @SerializedName("videos")
    private Videos videos;

    public ResponceMovieDetail(Videos videos) {
        this.videos = videos;
    }

    public Videos getVideos() {
        return videos;
    }

    @Override
    public String toString() {
        return "ResponceMovieDetail{" +
                "videos=" + videos +
                '}';
    }
}
