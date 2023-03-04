package com.example.movies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.ReviewAdapter;
import com.example.movies.TrailerAdapter;
import com.example.movies.database.Database;
import com.example.movies.database.MovieDao;
import com.example.movies.pojo.Movie;
import com.example.movies.pojo.Review;
import com.example.movies.pojo.Trailer;
import com.example.movies.viewModel.MovieDetailViewModel;

import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageViewPosterDetail;
    private ImageView imageViewStar;
    private TextView textViewNameDetail;
    private TextView textViewYearDetail;
    private TextView textViewDescriptionDetail;
    private RecyclerView recyclerViewTrailer;
    private RecyclerView recyclerViewReview;
    private ProgressBar progressBarTrailers;
    private static final String MOVIE_EXTRA = "movie";
    private static final String LOG_TAG = "MovieDetailActivity";
    private MovieDetailViewModel movieDetailViewModel;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initViews();

        Movie movie = (Movie) getIntent().getSerializableExtra(MOVIE_EXTRA);
        textViewNameDetail.setText(movie.getName());
        textViewYearDetail.setText(String.valueOf(movie.getYear()));
        textViewDescriptionDetail.setText(movie.getDescription());
        Glide.with(this)
                .load(movie.getPoster().getUrl())
                .into(imageViewPosterDetail);

        trailerAdapter = new TrailerAdapter();
        recyclerViewTrailer.setAdapter(trailerAdapter);

        reviewAdapter = new ReviewAdapter();
        recyclerViewReview.setAdapter(reviewAdapter);

        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);

        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void OnClick(Trailer trailer) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(trailer.getUrl()));
                startActivity(intent);
            }
        });

        movieDetailViewModel.getTrailersLiveData().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                trailerAdapter.setTrailers(trailers);
                Log.d(LOG_TAG, trailers.toString());
            }
        });

        movieDetailViewModel.getReviewsLiveData().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                reviewAdapter.setReviews(reviews);
                Log.d(LOG_TAG, "movieId: " + movie.getId() + "" + reviews.toString());
            }
        });

        movieDetailViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    progressBarTrailers.setVisibility(View.VISIBLE);
                } else {
                    progressBarTrailers.setVisibility(View.GONE);
                }
            }
        });

        movieDetailViewModel.loadTrailers(movie.getId());
        movieDetailViewModel.loadReviews(movie.getId());

        Drawable starOn = ContextCompat.getDrawable(MovieDetailActivity.this, android.R.drawable.star_big_on);
        Drawable starOff = ContextCompat.getDrawable(MovieDetailActivity.this, android.R.drawable.star_big_off);

        movieDetailViewModel.getFavoriteMovie(movie.getId()).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movieFromDB) {
                if (movieFromDB == null){
                    imageViewStar.setImageDrawable(starOff);
                    imageViewStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            movieDetailViewModel.insertMovie(movie);
                        }
                    });
                } else {
                    imageViewStar.setImageDrawable(starOn);
                    imageViewStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            movieDetailViewModel.removeMovie(movie.getId());
                        }
                    });
                }
            }
        });

        Log.d(LOG_TAG, movie.getName());
    }

    private void initViews() {
        imageViewPosterDetail = findViewById(R.id.imageViewPosterDetail);
        textViewNameDetail = findViewById(R.id.textViewNameDetail);
        textViewYearDetail = findViewById(R.id.textViewYearDetail);
        textViewDescriptionDetail = findViewById(R.id.textViewDescriptionDetail);
        recyclerViewTrailer = findViewById(R.id.recyclerViewTrailers);
        progressBarTrailers = findViewById(R.id.progressBarTrailers);
        recyclerViewReview = findViewById(R.id.recyclerViewReviews);
        imageViewStar = findViewById(R.id.imageViewStar);
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE_EXTRA, movie);
        return intent;
    }
}