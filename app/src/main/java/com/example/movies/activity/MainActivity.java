package com.example.movies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.MovieAdapter;
import com.example.movies.R;
import com.example.movies.pojo.Movie;
import com.example.movies.viewModel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = "MainActivity";
    private RecyclerView recyclerViewMovies;
    private ProgressBar progressBarMovies;
    private MovieAdapter movieAdapter;

    private MainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        movieAdapter = new MovieAdapter();
        movieAdapter.setOnClickListener(new MovieAdapter.OnClickListener() {
            @Override
            public void OnClick(Movie movie) {
                Intent intent = MovieDetailActivity.newIntent(MainActivity.this, movie);
                startActivity(intent);
            }
        });
        recyclerViewMovies.setAdapter(movieAdapter);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 2));

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getMoviesLiveData().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Log.d(LOG_TAG, movies.toString());
                movieAdapter.setMovies(movies);
            }
        });
        mainViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading){
                    progressBarMovies.setVisibility(View.VISIBLE);
                } else {
                    progressBarMovies.setVisibility(View.GONE);
                }
            }
        });
        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void OnReachEnd() {
                mainViewModel.load();
            }
        });
        mainViewModel.getIsError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                if (isError){
                    Toast.makeText(MainActivity.this, getString(R.string.toast_no_internet), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initViews(){
        recyclerViewMovies = findViewById(R.id.recyclerView);
        progressBarMovies = findViewById(R.id.progressBarMovies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemFavorite){
            startActivity(FavoriteMoviesActivity.newIntent(this));
        }
        return super.onOptionsItemSelected(item);
    }
}