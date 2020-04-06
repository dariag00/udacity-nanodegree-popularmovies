package com.example.popularmovies.root;

import com.example.popularmovies.MainActivity;
import com.example.popularmovies.http.MoviesModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, MoviesModule.class})
public interface ApplicationComponent {
    void inject(MainActivity target);
}
