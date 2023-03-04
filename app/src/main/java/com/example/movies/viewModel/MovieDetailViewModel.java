package com.example.movies.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movies.database.Database;
import com.example.movies.database.MovieDao;
import com.example.movies.internet.ApiFactory;
import com.example.movies.pojo.Movie;
import com.example.movies.pojo.ResponceMovieDetail;
import com.example.movies.pojo.ResponseReviews;
import com.example.movies.pojo.Review;
import com.example.movies.pojo.Trailer;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {

    private final String LOG_TAG = "MovieDetailViewModel";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<Trailer>> trailersLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Review>> reviewsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MovieDao movieDao;

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<Review>> getReviewsLiveData() {
        return reviewsLiveData;
    }

    public LiveData<List<Trailer>> getTrailersLiveData() {
        return trailersLiveData;
    }

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieDao = Database.getInstance(getApplication()).movieDao();
    }

    public LiveData<Movie> getFavoriteMovie(int id) {
        return movieDao.getFavoriteMovieById(id);
    }

    public void insertMovie(Movie movie) {
        Disposable disposable = movieDao.insertMovie(movie)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    public void removeMovie(int id) {
        Disposable disposable = movieDao.removeMovieById(id)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    public void loadTrailers(int movieId) {
        Disposable disposable = ApiFactory.apiService.loadTrailers(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .map(new Function<ResponceMovieDetail, List<Trailer>>() { // <-- переводит один объект в другой
                    @Override
                    public List<Trailer> apply(ResponceMovieDetail responceMovieDetail) throws Throwable {
                        return responceMovieDetail.getVideos().getTrailers();
                    }
                })
                .subscribe(new Consumer<List<Trailer>>() {
                    @Override
                    public void accept(List<Trailer> trailers) throws Throwable {
                        trailersLiveData.setValue(trailers);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(LOG_TAG, throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void loadReviews(int movieId) {
        Disposable disposable = ApiFactory.apiService.loadReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .map(new Function<ResponseReviews, List<Review>>() {
                    @Override
                    public List<Review> apply(ResponseReviews responseReviews) throws Throwable {
                        Log.d(LOG_TAG, responseReviews.getReviews().get(2).toString());
                        return responseReviews.getReviews();
                    }
                })
                .subscribe(new Consumer<List<Review>>() {
                    @Override
                    public void accept(List<Review> reviews) throws Throwable {
                        reviewsLiveData.setValue(reviews);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(LOG_TAG, throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
