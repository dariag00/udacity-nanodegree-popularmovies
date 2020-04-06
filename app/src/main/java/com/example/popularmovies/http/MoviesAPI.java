package com.example.popularmovies.http;

import com.example.popularmovies.http.movies.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesAPI {

    @GET("movie/popular")
    Call<MovieList> getMoviesByPopularity(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieList> getMoviesByTopRated(@Query("api_key") String apiKey);

}
