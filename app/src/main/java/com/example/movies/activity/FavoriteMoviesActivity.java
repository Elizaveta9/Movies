package com.example.movies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.movies.MovieAdapter;
import com.example.movies.R;
import com.example.movies.pojo.Movie;
import com.example.movies.viewModel.FavoriteMoviesViewModel;

import java.util.List;

public class FavoriteMoviesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavoriteMovies;
    private FavoriteMoviesViewModel viewModel;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        initViews();

        viewModel = new ViewModelProvider(this).get(FavoriteMoviesViewModel.class);

        movieAdapter = new MovieAdapter();
        movieAdapter.setOnClickListener(new MovieAdapter.OnClickListener() {
            @Override
            public void OnClick(Movie movie) {
                Intent intent = MovieDetailActivity.newIntent(
                        FavoriteMoviesActivity.this,
                        movie
                );
                startActivity(intent);
            }
        });
        recyclerViewFavoriteMovies.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewFavoriteMovies.setAdapter(movieAdapter);

        viewModel.getAllFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieAdapter.setMovies(movies);
            }
        });
    }

    public static Intent newIntent(Context context){
        return new Intent(context, FavoriteMoviesActivity.class);
    }

    private void initViews() {
        recyclerViewFavoriteMovies = findViewById(R.id.recyclerViewFavoriteMovies);
    }
}