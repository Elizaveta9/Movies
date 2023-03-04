package com.example.movies;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies.pojo.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies = new ArrayList<>();
    private OnReachEndListener onReachEndListener;
    private OnClickListener onClickListener;
    private final String LOG_TAG = "MovieAdapter";

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.movie_item,
                parent,
                false
        );
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder:" + position);
        Movie movie = movies.get(position);
        Glide.with(holder.itemView)
                .load(movie.getPoster().getUrl())
                .into(holder.imageViewPoster);
        double rating = movie.getRating().getKp();
        int backgroundCircleId;
        if (rating >= 7) {
            backgroundCircleId = R.drawable.circle_green;
        } else if (rating >= 5) {
            backgroundCircleId = R.drawable.circle_orange;
        } else {
            backgroundCircleId = R.drawable.circle_red;
        }
        Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), backgroundCircleId);
        holder.textViewRating.setBackground(drawable);
        String ratingString = String.format("%.1f", rating);
        holder.textViewRating.setText(ratingString);

        if ((position >= movies.size() - 10) && (onReachEndListener != null)){
            onReachEndListener.OnReachEnd();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null){
                    onClickListener.OnClick(movie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnReachEndListener {
        void OnReachEnd();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewPoster;

        private TextView textViewRating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            textViewRating = itemView.findViewById(R.id.textViewRating);
        }

    }
    public interface OnClickListener {
        void OnClick(Movie movie);
    }
}
