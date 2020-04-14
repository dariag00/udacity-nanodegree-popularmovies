package com.example.popularmovies.data.database;


import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.popularmovies.data.http.movies.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static final String LOG_TAG = MoviesDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "movies";

    private static final Object LOCK = new Object();
    private static MoviesDatabase instance;

    public static MoviesDatabase getInstance(Context context){
        Log.d(LOG_TAG, "Getting the database");
        if(instance == null){
            synchronized (LOCK){
                instance = Room.databaseBuilder(context.getApplicationContext(), MoviesDatabase.class,
                        MoviesDatabase.DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return instance;
    }

    public abstract MovieDao movieDao();

}
