package com.example.popularmovies.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.popularmovies.data.http.movies.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovieToFavorites(Movie movieEntry);

    @Query("SELECT * FROM movies WHERE id= :movieId")
    LiveData<Movie> getMovieById(int movieId);

    @Delete
    void deleteMovie(Movie movie);

}
