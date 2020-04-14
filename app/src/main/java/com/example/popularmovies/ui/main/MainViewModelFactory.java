package com.example.popularmovies.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.popularmovies.data.MoviesRepository;

public class MainViewModelFactory  extends ViewModelProvider.NewInstanceFactory  {

    private final MoviesRepository moviesRepository;
    private final String orderType;

    public MainViewModelFactory(MoviesRepository moviesRepository, String orderType){
        this.moviesRepository = moviesRepository;
        this.orderType = orderType;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(moviesRepository, orderType);
    }
}
