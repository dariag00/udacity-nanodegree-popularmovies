package com.example.popularmovies.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmovies.data.MoviesRepository;
import com.example.popularmovies.data.http.movies.ReviewList;
import com.example.popularmovies.data.http.movies.TrailerList;

public class DetailsViewModel extends ViewModel {

    private LiveData<ReviewList> reviewListLiveData;
    private LiveData<TrailerList> trailerListLiveData;

    public DetailsViewModel(MoviesRepository moviesRepository, int movieId){
        reviewListLiveData = moviesRepository.getReviewsOfMovie(movieId);
        trailerListLiveData = moviesRepository.getTrailersOfMovie(movieId);
    }

    public LiveData<ReviewList> getReviewListLiveData() {
        return reviewListLiveData;
    }

    public LiveData<TrailerList> getTrailerListLiveData() {
        return trailerListLiveData;
    }
}
