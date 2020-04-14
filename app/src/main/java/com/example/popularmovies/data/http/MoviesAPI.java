package com.example.popularmovies.data.http;


import com.example.popularmovies.data.http.movies.MovieList;
import com.example.popularmovies.data.http.movies.ReviewList;
import com.example.popularmovies.data.http.movies.TrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesAPI {

    @GET("movie/popular")
    Call<MovieList> getMoviesByPopularity(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieList> getMoviesByTopRated(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewList> getMovieReview(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<TrailerList> getMovieTrailers(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

}
