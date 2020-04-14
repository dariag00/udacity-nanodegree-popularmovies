package com.example.popularmovies.root;


import android.app.Application;

import com.example.popularmovies.data.http.MoviesModule;


public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .moviesModule(new MoviesModule())
                .build();
    }

    public ApplicationComponent getComponent(){
        return component;
    }

}
