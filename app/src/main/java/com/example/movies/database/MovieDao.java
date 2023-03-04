package com.example.movies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.movies.pojo.Movie;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM favorite_movie")
    LiveData<List<Movie>> getAllFavoriteMovies();

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    LiveData<Movie> getFavoriteMovieById(int id);

    @Insert
    Completable insertMovie(Movie movie);

    @Query("DELETE FROM favorite_movie WHERE id = :id")
    Completable removeMovieById(int id);
}
