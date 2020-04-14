package com.example.popularmovies.root;

import com.example.popularmovies.ui.detail.DetailsActivity;
import com.example.popularmovies.ui.main.MainActivity;
import com.example.popularmovies.data.http.MoviesModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, MoviesModule.class})
public interface ApplicationComponent {
    void inject(MainActivity target);
    void inject(DetailsActivity target);
}
