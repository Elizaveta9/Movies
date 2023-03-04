package com.example.movies.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {
    @SerializedName("docs")
    private List<Movie> movies;

    public Response(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return "Response{" +
                "movies=" + movies +
                '}';
    }
}
