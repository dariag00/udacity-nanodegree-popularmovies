package com.example.popularmovies.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.popularmovies.data.MoviesRepository;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MoviesRepository moviesRepository;
    private final int movieId;


    public DetailsViewModelFactory(MoviesRepository moviesRepository, int movieId){
        this.movieId = movieId;
        this.moviesRepository = moviesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(moviesRepository, movieId);
    }
}
