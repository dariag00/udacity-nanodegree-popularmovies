package com.example.popularmovies.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.popularmovies.AppExecutors;
import com.example.popularmovies.Constants;
import com.example.popularmovies.data.database.MovieDao;
import com.example.popularmovies.data.database.MoviesDatabase;
import com.example.popularmovies.data.http.MoviesAPI;
import com.example.popularmovies.data.http.movies.Movie;
import com.example.popularmovies.data.http.movies.MovieList;
import com.example.popularmovies.data.http.movies.ReviewList;
import com.example.popularmovies.data.http.movies.TrailerList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRepository {

    private static final String LOG_TAG = MoviesRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static MoviesRepository instance;

    private MovieDao movieDao;
    private MoviesAPI moviesAPI;

    private MoviesRepository(Application application, MoviesAPI moviesAPI){
        MoviesDatabase appDatabase = MoviesDatabase.getInstance(application);
        this.movieDao = appDatabase.movieDao();
        this.moviesAPI = moviesAPI;
    }

    public synchronized static MoviesRepository getInstance(Application application, MoviesAPI moviesAPI){
        Log.d(LOG_TAG, "Getting the repository");
        if(instance == null){
            synchronized (LOCK) {
                instance = new MoviesRepository(application, moviesAPI);
                Log.d(LOG_TAG, "Made new movies repository");
            }
        }

        return instance;
    }


    /*
     * Database related operations
     */

    public LiveData<List<Movie>> getFavoriteMovies(){
        return movieDao.getFavoriteMovies();
    }

    public void addMovieToFavorites(Movie movieEntry){
        AppExecutors.getInstance().diskIO().execute(() ->
            movieDao.addMovieToFavorites(movieEntry));
    }

    public LiveData<Movie> getMovieById(int id){
        return movieDao.getMovieById(id);
    }

    public void deleteMovie(Movie movie){
        AppExecutors.getInstance().diskIO().execute(() ->
            movieDao.deleteMovie(movie)
        );
    }

    /*
     * Network related operations
     */

    public LiveData<ReviewList> getReviewsOfMovie(int movieId){
        MutableLiveData<ReviewList> reviewsLiveData = new MutableLiveData<>();
        moviesAPI.getMovieReview(movieId, Constants.API_KEY).enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                if (response.code() == 200) {
                    reviewsLiveData.setValue(response.body());
                }else{
                    reviewsLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                reviewsLiveData.setValue(null);
            }
        });

        return reviewsLiveData;
    }

    public LiveData<TrailerList> getTrailersOfMovie(int movieId){
        MutableLiveData<TrailerList> trailersLiveData = new MutableLiveData<>();
        moviesAPI.getMovieTrailers(movieId, Constants.API_KEY).enqueue(new Callback<TrailerList>() {
            @Override
            public void onResponse(Call<TrailerList> call, Response<TrailerList> response) {
                if (response.code() == 200) {
                    trailersLiveData.setValue(response.body());
                }else{
                    trailersLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<TrailerList> call, Throwable t) {
                trailersLiveData.setValue(null);
            }
        });

        return trailersLiveData;
    }

    public MutableLiveData<List<Movie>> getMoviesByTopRated(){
        return getMovieLiveData(moviesAPI.getMoviesByTopRated(Constants.API_KEY));
    }

    public MutableLiveData<List<Movie>> getMoviesByPopularity(){
        return getMovieLiveData(moviesAPI.getMoviesByPopularity(Constants.API_KEY));
    }

    private MutableLiveData<List<Movie>> getMovieLiveData(Call<MovieList> moviesCall){
        MutableLiveData<List<Movie>> movieLiveData = new MutableLiveData<>();
        moviesCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.code() == 200) {
                    movieLiveData.setValue(response.body().getMovieList());
                }else{
                    movieLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                movieLiveData.setValue(null);
            }
        });

        return movieLiveData;
    }

}
