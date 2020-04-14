package com.example.popularmovies.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmovies.Constants;
import com.example.popularmovies.data.MoviesRepository;
import com.example.popularmovies.data.http.movies.Movie;

import java.util.List;

public class MainViewModel extends ViewModel {

    private LiveData<List<Movie>> moviesLiveData;
    private MoviesRepository moviesRepository;

    public MainViewModel(MoviesRepository moviesRepository, String orderType){
        this.moviesRepository = moviesRepository;
        updateLiveData(orderType);
    }

    public LiveData<List<Movie>> getMoviesLiveData() {
        return moviesLiveData;
    }

    public void updateLiveData(String orderType){
        switch (orderType) {
            case Constants.ORDER_BY_TOP_RATED:
                moviesLiveData = moviesRepository.getMoviesByTopRated();
                break;
            case Constants.ORDER_BY_POPULARITY:
                moviesLiveData = moviesRepository.getMoviesByPopularity();
                break;
            case Constants.ORDER_BY_FAVORITES:
                moviesLiveData = moviesRepository.getFavoriteMovies();
                break;
        }
    }
}
