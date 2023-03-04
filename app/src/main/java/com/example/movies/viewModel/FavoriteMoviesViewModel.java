package com.example.movies.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movies.database.Database;
import com.example.movies.database.MovieDao;
import com.example.movies.pojo.Movie;

import java.util.List;

public class FavoriteMoviesViewModel extends AndroidViewModel {
    private MovieDao movieDao;
    public FavoriteMoviesViewModel(@NonNull Application application) {
        super(application);
        movieDao = Database.getInstance(getApplication()).movieDao();
    }

    public LiveData<List<Movie>> getAllFavoriteMovies(){
        return movieDao.getAllFavoriteMovies();
    }
}
